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
package io.micronaut.starter.feature.function.awslambda;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.awsalexa.AwsAlexa;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.DocumentationLink;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.starter.feature.function.awslambda.template.*;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerWritable;

import jakarta.inject.Singleton;
import java.util.Set;

import static io.micronaut.starter.application.ApplicationType.DEFAULT;
import static io.micronaut.starter.application.ApplicationType.FUNCTION;

@Singleton
public class AwsLambda implements FunctionFeature, DefaultFeature, CloudFeature, HandlerClassFeature {

    public static final String FEATURE_NAME_AWS_LAMBDA = "aws-lambda";
    public static final String MICRONAUT_LAMBDA_HANDLER = "io.micronaut.function.aws.proxy.MicronautLambdaHandler";
    public static final String REQUEST_HANDLER = "FunctionRequestHandler";
    private static final String LINK_TITLE = "AWS Lambda Handler";
    private static final String LINK_URL = "https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html";

    private final ShadePlugin shadePlugin;
    private final AwsLambdaCustomRuntime customRuntime;

    public AwsLambda(ShadePlugin shadePlugin, AwsLambdaCustomRuntime customRuntime) {
        this.shadePlugin = shadePlugin;
        this.customRuntime = customRuntime;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(ShadePlugin.class)) {
            featureContext.addFeature(shadePlugin);
        }
        if (featureContext.isPresent(GraalVM.class) &&
                (
                        featureContext.getBuildTool() == BuildTool.MAVEN ||
                        (featureContext.getBuildTool().isGradle() && featureContext.getApplicationType() == FUNCTION)
                )
        ) {
            featureContext.addFeature(customRuntime);
        }
    }

    @Override
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for writing functions to deploy to AWS Lambda";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        if (shouldGenerateSources(generatorContext)) {
            ApplicationType applicationType = generatorContext.getApplicationType();
            if (applicationType == DEFAULT || applicationType == FUNCTION) {
                if (applicationType == DEFAULT) {
                    addHomeController(generatorContext, project);
                    addHomeControllerTest(generatorContext, project);
                } else {
                    addRequestHandler(generatorContext, project);
                    generatorContext.addDependency(Dependency.builder().lookupArtifactId("aws-lambda-java-events").compile());
                    addTest(generatorContext, project);
                }
                DocumentationLink link = new DocumentationLink(LINK_TITLE, LINK_URL);
                generatorContext.addHelpTemplate(new RockerWritable(readmeRockerModel(this, generatorContext, link)));
            }
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
                awsLambdaFunctionRequestHandlerJava.template(project),
                awsLambdaFunctionRequestHandlerKotlin.template(project),
                awsLambdaFunctionRequestHandlerGroovy.template(project));
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
    public Cloud getCloud() {
        return Cloud.AWS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda";
    }

    @Override
    public String handlerClass(ApplicationType applicationType, Project project) {
        switch (applicationType) {
            case DEFAULT:
                return MICRONAUT_LAMBDA_HANDLER;
            case FUNCTION:
                return project.getPackageName() + "." + REQUEST_HANDLER;
            default:
                return null;
        }
    }
}
