/*
 * Copyright 2017-2020 original authors
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
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyContext;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleDependency;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenBuild;
import io.micronaut.starter.build.maven.MavenDependency;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.MultiProjectFeature;
import io.micronaut.starter.feature.aws.template.cdkappstack;
import io.micronaut.starter.feature.build.maven.templates.genericPom;
import io.micronaut.starter.feature.build.maven.templates.mavenShadePlugin;
import io.micronaut.starter.feature.build.gradle.templates.genericBuildGradle;
import io.micronaut.starter.feature.aws.template.cdkjson;
import io.micronaut.starter.feature.aws.template.cdkmain;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public class Cdk implements MultiProjectFeature {
    public static final String INFRA_MODULE = "infra";
    public static final String NAME = "aws-cdk";
    private static final String MAIN_CLASS_NAME = "Main";
    private final DependencyContext dependencyContext;

    public Cdk(CoordinateResolver coordinateResolver) {
        this.dependencyContext = new DependencyContextImpl(coordinateResolver);
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
        generatorContext.addTemplate("cdk-json", new RockerTemplate(INFRA_MODULE, "cdk.json", cdkjson.template()));
        generatorContext.addTemplate("cdk-main", new RockerTemplate(INFRA_MODULE, "src/main/java/{packagePath}/" + MAIN_CLASS_NAME + ".java",
                cdkmain.template(generatorContext.getProject())));
        generatorContext.addTemplate("cdk-appstack", new RockerTemplate(INFRA_MODULE, "src/main/java/{packagePath}/AppStack.java",
                cdkappstack.template(generatorContext.getProject())));
        buildRockerModel(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("cdk-build",
                    new RockerTemplate(INFRA_MODULE, generatorContext.getBuildTool().getBuildFileName(), rockerModel));
        });
    }

    private Optional<RockerModel> buildRockerModel(GeneratorContext generatorContext) {
        dependencyContext.addDependency(Dependency.builder()
                .lookupArtifactId("aws-cdk-lib")
                .compile()
                .build());

        RockerModel rockerModel = null;
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            rockerModel = genericPom.template(generatorContext.getProject(), infrastructureMavenBuild(generatorContext));
        } else if (generatorContext.getBuildTool().isGradle()) {
            rockerModel = genericBuildGradle.template(generatorContext.getProject(), infrastructureGradleBuild(generatorContext), MAIN_CLASS_NAME);
        }
        return Optional.ofNullable(rockerModel);
    }

    private MavenBuild infrastructureMavenBuild(GeneratorContext generatorContext) {
        List<MavenDependency> dependencies = MavenDependency.listOf(dependencyContext);
        List<MavenPlugin> plugins = Collections.singletonList(MavenPlugin.builder()
                .artifactId("artifactId>maven-shade-plugin")
                .extension(new RockerWritable(mavenShadePlugin.template(generatorContext.getProject(), MAIN_CLASS_NAME)))
                .build());
        return new MavenBuild(generatorContext.getProject().getName() + "-" + INFRA_MODULE,
                dependencies,
                plugins);
    }

    private GradleBuild infrastructureGradleBuild(GeneratorContext generatorContext) {
        List<GradlePlugin> plugins = new ArrayList<>();
        plugins.add(GradlePlugin.builder().id("application").build());
        plugins.add(GradlePlugin.builder().id("java").build());
        return new GradleBuild(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY),
                GradleDependency.listOf(dependencyContext, generatorContext),
                plugins);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/cdk/v2/guide/home.html";
    }
}
