/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.aws;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.DependencyContextImpl;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleDependency;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.gradle.GradleRepository;
import io.micronaut.starter.build.maven.MavenBuild;
import io.micronaut.starter.build.maven.MavenCombineAttribute;
import io.micronaut.starter.build.maven.MavenDependency;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.build.maven.MavenRepository;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.InfrastructureAsCodeFeature;
import io.micronaut.starter.feature.MultiProjectFeature;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.template.cdkappstack;
import io.micronaut.starter.feature.aws.template.cdkappstacktest;
import io.micronaut.starter.feature.aws.template.cdkhelp;
import io.micronaut.starter.feature.aws.template.cdkjson;
import io.micronaut.starter.feature.aws.template.cdkmain;
import io.micronaut.starter.feature.aws.template.testlambda;
import io.micronaut.starter.feature.build.MicronautAot;
import io.micronaut.starter.feature.build.gradle.templates.genericBuildGradle;
import io.micronaut.starter.feature.build.gradle.templates.useJunitPlatform;
import io.micronaut.starter.feature.build.maven.templates.execMavenPlugin;
import io.micronaut.starter.feature.build.maven.templates.genericPom;
import io.micronaut.starter.feature.build.maven.templates.mavenCompilerPlugin;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.function.awslambda.DefaultAwsLambdaHandlerProvider;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public class Cdk implements MultiProjectFeature, InfrastructureAsCodeFeature {
    public static final String INFRA_MODULE = "infra";
    public static final String NAME = "aws-cdk";
    private static final String MAIN_CLASS_NAME = "Main";
    private static final String ARTIFACT_ID_AWS_CDK_LIB = "aws-cdk-lib";
    private static final String GROUP_ID_SOFTWARE_AMAZON_AWSCDK = "software.amazon.awscdk";
    private static final String ARTIFACT_ID_MICRONAUT_STARTER_AWS_CDK = "micronaut-starter-aws-cdk";
    private final CpuArchitecture defaultCpuArchitecture;
    private final DependencyContext dependencyContext;
    private final RepositoryResolver repositoryResolver;
    private final CoordinateResolver coordinateResolver;
    private final Dependency DEPENDENCY_CDK;

    public Cdk(CoordinateResolver coordinateResolver,
               X86 x86,
               RepositoryResolver repositoryResolver) {
        this.coordinateResolver = coordinateResolver;
        DEPENDENCY_CDK = MicronautDependencyUtils.starterDependency()
                        .artifactId(ARTIFACT_ID_MICRONAUT_STARTER_AWS_CDK)
                        .version(coordinateResolver.resolve(ARTIFACT_ID_MICRONAUT_STARTER_AWS_CDK).map(Coordinate::getVersion).orElseThrow())
                    .exclude(Dependency.builder()
                .groupId(GROUP_ID_SOFTWARE_AMAZON_AWSCDK)
                .artifactId(ARTIFACT_ID_AWS_CDK_LIB)
                .build())
                .compile()
                .build();
        this.defaultCpuArchitecture = x86;
        this.dependencyContext = new DependencyContextImpl(coordinateResolver);
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "AWS CDK (Cloud Development Kit)";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Amazon Web Services CDK (Cloud Development Kit) support";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.FUNCTION;
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().hasFeature(AwsApiFeature.class)) {
            generatorContext.addTemplate("test-lambda", new RockerTemplate(Template.ROOT, "test-lambda.sh", testlambda.template(generatorContext.getBuildTool(), generatorContext.getFeatures().hasFeature(GraalVM.class), INFRA_MODULE), true));
        }
        generatorContext.addTemplate("cdk-json", new RockerTemplate(INFRA_MODULE, "cdk.json", cdkjson.template(generatorContext.getBuildTool(), INFRA_MODULE)));
        generatorContext.addTemplate("cdk-main", new RockerTemplate(INFRA_MODULE, "src/main/java/{packagePath}/" + MAIN_CLASS_NAME + ".java",
                cdkmain.template(generatorContext.getProject())));

        String handler = HandlerClassFeature.resolveHandler(generatorContext);
        Language lang = Language.JAVA;
        addAppStackTest(generatorContext, lang, handler);
        CpuArchitecture architecture = generatorContext.getFeatures().getFeature(CpuArchitecture.class)
                .orElse(defaultCpuArchitecture);
        generatorContext.addTemplate("cdk-appstack", new RockerTemplate(INFRA_MODULE, lang.getSrcDir() + "/{packagePath}/AppStack.java",
                cdkappstack.template(generatorContext.getFeatures(),
                        generatorContext.getProject(),
                        generatorContext.getBuildTool(),
                        generatorContext.getApplicationType(),
                        architecture,
                        Template.DEFAULT_MODULE,
                        generatorContext.getBuildTool().isGradle() ? "build/libs" : "target",
                        generatorContext.getFeatures().hasFeature(AwsLambda.class) ? "micronaut-function" : null,
                        generatorContext.getFeatures().hasFeature(AwsApiFeature.class) ? "micronaut-function-api" : null,
                        "0.1",
                        handler,
                        generatorContext.getFeatures().hasFeature(GraalVM.class),
                        generatorContext.getFeatures().hasFeature(MicronautAot.class),
                        generatorContext.getJdkVersion()))
        );
        buildRockerModel(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("cdk-build",
                    new RockerTemplate(INFRA_MODULE, generatorContext.getBuildTool().getBuildFileName(), rockerModel));
        });

        generatorContext.addHelpTemplate(new RockerWritable(cdkhelp.template(generatorContext.getBuildTool(), generatorContext.getFeatures().hasFeature(GraalVM.class), INFRA_MODULE)));
    }

    protected void addAppStackTest(@NonNull GeneratorContext generatorContext,
                                   @NonNull Language lang,
                                   @NonNull String handler) {
        generatorContext.addTemplate("cdk-appstacktest", new RockerTemplate(INFRA_MODULE, lang.getTestSrcDir() + "/{packagePath}/AppStackTest.java",
                cdkappstacktest.template(generatorContext.getProject(), handler)));

    }

    private void populateDependencies(GeneratorContext generatorContext) {
        dependencyContext.addDependency(bomDependency().compile());
        dependencyContext.addDependency(DEPENDENCY_CDK);
        coordinateResolver.resolve(ARTIFACT_ID_AWS_CDK_LIB)
                .ifPresent(coordinate -> dependencyContext.addDependency(Dependency.builder()
                        .groupId(coordinate.getGroupId())
                        .artifactId(coordinate.getArtifactId())
                        .version(coordinate.getVersion())
                        .compile()
                        .build()));
        dependencyContext.addDependency(bomDependency().test());
        dependencyContext.addDependency(Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .test());
        dependencyContext.addDependency(Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-engine")
                .test());
        if (generatorContext.getFeatures().hasFeature(AmazonApiGatewayHttp.class)) {
            dependencyContext.addDependency(Dependency.builder()
                    .lookupArtifactId("apigatewayv2-alpha")
                    .compile());
            dependencyContext.addDependency(Dependency.builder()
                    .lookupArtifactId("apigatewayv2-integrations-alpha")
                    .compile());
        }
    }

    private Dependency.Builder bomDependency() {
        return MicronautDependencyUtils.platformDependency()
                .artifactId("micronaut-platform")
                .version(VersionInfo.getMicronautVersion())
                .pom();
    }

    private Optional<RockerModel> buildRockerModel(GeneratorContext generatorContext) {
        populateDependencies(generatorContext);
        RockerModel rockerModel = null;
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            rockerModel = genericPom.template(generatorContext.getProject(), infrastructureMavenBuild(generatorContext));
        } else if (generatorContext.getBuildTool().isGradle()) {
            rockerModel = genericBuildGradle.template(generatorContext.getProject(), infrastructureGradleBuild(generatorContext), MAIN_CLASS_NAME, null, null);
        }
        return Optional.ofNullable(rockerModel);
    }

    private MavenBuild infrastructureMavenBuild(GeneratorContext generatorContext) {
        List<MavenDependency> dependencies = MavenDependency.listOf(dependencyContext, generatorContext.getLanguage());
        List<MavenPlugin> plugins = Arrays.asList(
                MavenPlugin.builder()
                        .artifactId("maven-compiler-plugin")
                        .extension(new RockerWritable(mavenCompilerPlugin.template()))
                        .build(),
                MavenPlugin.builder()
                        .artifactId("exec-maven-plugin")
                        .extension(new RockerWritable(execMavenPlugin.template(generatorContext.getProject().getPackageName() + "." + MAIN_CLASS_NAME)))
                        .build());
        List<Property> properties = Collections.singletonList(new Property() {
            @Override
            public String getKey() {
                return "jdk.version";
            }

            @Override
            public String getValue() {
                return generatorContext.getFeatures().getTargetJdk();
            }
        });
        return new MavenBuild(generatorContext.getProject().getName() + "-" + INFRA_MODULE,
                Collections.emptyList(),
                Collections.emptyList(),
                dependencies,
                properties,
                plugins,
                MavenRepository.listOf(repositoryResolver.resolveRepositories(generatorContext)),
                MavenCombineAttribute.APPEND,
                MavenCombineAttribute.APPEND,
                Collections.emptyList());
    }

    private GradleBuild infrastructureGradleBuild(GeneratorContext generatorContext) {
        List<GradlePlugin> plugins = new ArrayList<>();
        plugins.add(GradlePlugin.builder().id("application")
                        .extension(new RockerTemplate(useJunitPlatform.template(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY))))
                .build());
        plugins.add(GradlePlugin.builder().id("java").build());
        return new GradleBuild(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY),
                GradleDependency.listOf(dependencyContext, generatorContext, useVersionCatalog()),
                plugins,
                GradleRepository.listOf(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY),
                        repositoryResolver.resolveRepositories(generatorContext)));
    }

    /**
     *
     * @return Whether the build should use Micronaut Gradle Version Catalog.
     */
    public boolean useVersionCatalog() {
        return false;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/cdk/v2/guide/home.html";
    }
}
