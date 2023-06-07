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
package io.micronaut.starter.feature.function.awslambda;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.AwsApiFeature;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.starter.feature.aws.AwsLambdaEventFeature;
import io.micronaut.starter.feature.aws.AwsLambdaSnapstart;
import io.micronaut.starter.feature.aws.AwsMicronautRuntimeFeature;
import io.micronaut.starter.feature.awsalexa.AwsAlexa;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.DocumentationLink;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerGroovyJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerJavaJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerKoTest;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerKotlinJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaFunctionRequestHandlerSpock;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerGroovy;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerGroovyJunit;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerJava;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerJavaJunit;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerKoTest;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerKotlin;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerKotlinJunit;
import io.micronaut.starter.feature.function.awslambda.template.homeControllerSpock;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.feature.security.SecurityFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static io.micronaut.starter.application.ApplicationType.DEFAULT;
import static io.micronaut.starter.application.ApplicationType.FUNCTION;
import static io.micronaut.starter.feature.crac.Crac.DEPENDENCY_MICRONAUT_CRAC;

@Singleton
public class AwsLambda implements FunctionFeature, DefaultFeature, AwsCloudFeature, AwsMicronautRuntimeFeature {

    public static final String FEATURE_NAME_AWS_LAMBDA = "aws-lambda";
    public static final String REQUEST_HANDLER = "FunctionRequestHandler";
    public static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_TEST = MicronautDependencyUtils.coreDependency()
            .artifactId("micronaut-function")
            .test()
            .build();

    private static final String LINK_TITLE = "AWS Lambda Handler";
    private static final String LINK_URL = "https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html";
    private static final Dependency AWS_LAMBDA_JAVA_EVENTS = Dependency.builder().lookupArtifactId("aws-lambda-java-events").compile().build();
    private static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_AWS = MicronautDependencyUtils.awsDependency()
            .artifactId("micronaut-function-aws")
            .compile()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY = MicronautDependencyUtils.awsDependency()
            .artifactId("micronaut-function-aws-api-proxy")
            .compile()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY_TEST = MicronautDependencyUtils.awsDependency()
            .artifactId("micronaut-function-aws-api-proxy-test")
            .compile()
            .build();

    private final ShadePlugin shadePlugin;
    private final AwsLambdaCustomRuntime customRuntime;
    private final CpuArchitecture defaultCpuArchitecture;
    private final AwsLambdaSnapstart snapstart;

    private final HandlerClassFeature defaultAwsLambdaHandlerProvider;
    private final HandlerClassFeature functionAwsLambdaHandlerProvider;

    public AwsLambda(ShadePlugin shadePlugin,
                     AwsLambdaCustomRuntime customRuntime,
                     X86 x86,
                     AwsLambdaSnapstart snapstart,
                     DefaultAwsLambdaHandlerProvider defaultAwsLambdaHandlerProvider,
                     FunctionAwsLambdaHandlerProvider functionAwsLambdaHandlerProvider) {
        this.shadePlugin = shadePlugin;
        this.customRuntime = customRuntime;
        this.defaultCpuArchitecture = x86;
        this.snapstart = snapstart;
        this.defaultAwsLambdaHandlerProvider = defaultAwsLambdaHandlerProvider;
        this.functionAwsLambdaHandlerProvider = functionAwsLambdaHandlerProvider;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        Stream.of(defaultAwsLambdaHandlerProvider, functionAwsLambdaHandlerProvider)
                .filter(f -> f.supports(featureContext.getApplicationType()))
                .findFirst()
                .ifPresent(f -> featureContext.addFeatureIfNotPresent(HandlerClassFeature.class, f));

        featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
        featureContext.addFeatureIfNotPresent(CpuArchitecture.class, defaultCpuArchitecture);
        if (featureContext.isPresent(GraalVM.class) &&
                (
                        featureContext.getBuildTool() == BuildTool.MAVEN ||
                        (featureContext.getBuildTool().isGradle() && featureContext.getApplicationType() == FUNCTION)
                )
        ) {
            featureContext.addFeature(customRuntime);
        }

        if (shouldAddSnapstartFeature(featureContext)) {
            featureContext.addFeature(snapstart);
        }
    }

    protected boolean shouldAddSnapstartFeature(FeatureContext featureContext) {
        if (featureContext.isPresent(GraalVM.class)) {
            return false;
        }
        return featureContext.getFeature(CpuArchitecture.class)
                .filter(CpuArchitecture.class::isInstance)
                .map(CpuArchitecture.class::cast)
                .map(snapstart::supports)
                .orElse(true);
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to AWS Lambda";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (shouldGenerateSources(generatorContext)) {
            ApplicationType applicationType = generatorContext.getApplicationType();
            if (applicationType == DEFAULT || applicationType == FUNCTION) {
                addCode(generatorContext);
                if (applicationType == FUNCTION) {
                    generatorContext.addDependency(AWS_LAMBDA_JAVA_EVENTS);
                }
                addHelpTemplate(generatorContext);
                disableSecurityFilterInTestConfiguration(generatorContext);
            }
        }
        addMicronautRuntimeBuildProperty(generatorContext);
        addDependencies(generatorContext);
    }

    private void addDependencies(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_AWS);
        }
        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getApplicationType() == DEFAULT) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY);
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY_TEST);
        }
        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.hasFeature(GraalVM.class)) {
            generatorContext.addDependency(AwsLambdaCustomRuntime.DEPENDENCY_AWS_FUNCTION_AWS_CUSTOM_RUNTIME);
        }

        if (generatorContext.hasFeature(AwsLambdaSnapstart.class)) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_CRAC);
        }

        if (generatorContext.getFeatures().testFramework().isSpock() &&
                generatorContext.getBuildTool().isGradle()) {
            // maven has this in parent pom
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_TEST);
        }
    }

    protected void addCode(@NonNull GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        ApplicationType applicationType = generatorContext.getApplicationType();
        if (applicationType == DEFAULT) {
            addHomeController(generatorContext, project);
            addHomeControllerTest(generatorContext, project);
        } else if (applicationType == FUNCTION) {
            addRequestHandler(generatorContext, project);
            if (generatorContext.getFeatures().hasFeature(AwsApiFeature.class) ||
                    !generatorContext.getFeatures().hasFeature(AwsLambdaEventFeature.class)) {
                addTest(generatorContext, project);
            }
        }
    }

    protected void addHelpTemplate(@NonNull GeneratorContext generatorContext) {
        readmeTemplate(generatorContext)
                .ifPresent(rockerModel -> generatorContext.addHelpTemplate(new RockerWritable(rockerModel)));
    }

    @NonNull
    public Optional<RockerModel> readmeTemplate(@NonNull GeneratorContext generatorContext) {
        DocumentationLink link = new DocumentationLink(LINK_TITLE, LINK_URL);
        return generatorContext.getFeature(HandlerClassFeature.class)
                .map(f -> HandlerClassFeature.readmeRockerModel(f, generatorContext, link));
    }

    protected void disableSecurityFilterInTestConfiguration(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().hasFeature(SecurityFeature.class)) {
            ApplicationConfiguration test = generatorContext.getConfiguration(Environment.FUNCTION, ApplicationConfiguration.functionTestConfig());
            test.put("micronaut.security.filter.enabled", false);
        }
    }

    boolean shouldGenerateSources(GeneratorContext generatorContext) {
        return !generatorContext.getFeatures().isFeaturePresent(AwsAlexa.class);
    }

    private void addHomeControllerTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/HomeController");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(homeControllerSpock.template(project),
                homeControllerJavaJunit.template(project),
                homeControllerGroovyJunit.template(project),
                homeControllerKotlinJunit.template(project),
                homeControllerKoTest.template(project));
        generatorContext.addTemplate("testHomeController", testSource, provider);
    }

    private void addHomeController(GeneratorContext generatorContext, Project project) {
        String controllerFile = generatorContext.getSourcePath("/{packagePath}/HomeController");
        generatorContext.addTemplate("homeController", controllerFile,
                homeControllerJava.template(project),
                homeControllerKotlin.template(project),
                homeControllerGroovy.template(project));
    }

    private void addTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/FunctionRequestHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(awsLambdaFunctionRequestHandlerSpock.template(project),
                awsLambdaFunctionRequestHandlerJavaJunit.template(project),
                awsLambdaFunctionRequestHandlerGroovyJunit.template(project),
                awsLambdaFunctionRequestHandlerKotlinJunit.template(project),
                awsLambdaFunctionRequestHandlerKoTest.template(project));
        generatorContext.addTemplate("testFunctionRequestHandler", testSource, provider);
    }

    private void addRequestHandler(GeneratorContext generatorContext, Project project) {
        String awsLambdaRequestHandlerFile = generatorContext.getSourcePath("/{packagePath}/" + REQUEST_HANDLER);
        generatorContext.addTemplate("functionRequestHandler", awsLambdaRequestHandlerFile,
                awsLambdaFunctionRequestHandlerJava.template(generatorContext.getFeatures(), project),
                awsLambdaFunctionRequestHandlerKotlin.template(generatorContext.getFeatures(), project),
                awsLambdaFunctionRequestHandlerGroovy.template(generatorContext.getFeatures(), project));
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == FUNCTION &&
                selectedFeatures.stream().filter(feature -> feature instanceof CloudFeature)
                        .noneMatch(cloudFeature -> ((CloudFeature) cloudFeature).getCloud() != getCloud());
    }

    @Override
    public String getCategory() {
        return Category.SERVERLESS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda";
    }
}

