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
package io.micronaut.starter.feature.awslambdacustomruntime;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.awsCustomRuntimeReadme;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.functionLambdaRuntimeGroovy;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.functionLambdaRuntimeJava;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.functionLambdaRuntimeKotlin;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.other.HttpClient;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class AwsLambdaCustomRuntime implements FunctionFeature, ApplicationFeature, AwsCloudFeature {
    public static final Dependency DEPENDENCY_AWS_FUNCTION_AWS_CUSTOM_RUNTIME = MicronautDependencyUtils.awsDependency()
            .artifactId("micronaut-function-aws-custom-runtime")
            .compile()
            .build();
    public static final String MAIN_CLASS_NAME = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime";

    public static final String FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME = "aws-lambda-custom-runtime";

    private final Provider<AwsLambda> awsLambda;
    private final HttpClient httpClient;

    public AwsLambdaCustomRuntime(Provider<AwsLambda> awsLambda, HttpClient httpClient) {
        this.awsLambda = awsLambda;
        this.httpClient = httpClient;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        AwsLambda awsLambda = this.awsLambda.get();
        if (awsLambda.supports(featureContext.getApplicationType()) && !featureContext.isPresent(AwsLambda.class)) {
            featureContext.addFeature(awsLambda);
        }
        if (!featureContext.isPresent(HttpClient.class)) {
            featureContext.addFeature(httpClient);
        }
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Custom AWS Lambda runtime";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for deploying a Micronaut Function to a Custom AWS Lambda Runtime";
    }

    @SuppressWarnings("EmptyBlock")
    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationFeature.super.apply(generatorContext);
        Project project = generatorContext.getProject();
        if (shouldGenerateMainClassForRuntime(generatorContext)) {
            addFunctionLambdaRuntime(generatorContext, project);
        }

        if (generatorContext.getFeatures().isFeaturePresent(GraalVM.class)) {
            generatorContext.addHelpTemplate(new RockerWritable(awsCustomRuntimeReadme.template(generatorContext.getBuildTool())));
        }
        addDependencies(generatorContext);
    }

    private void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_AWS_FUNCTION_AWS_CUSTOM_RUNTIME);
    }

    public boolean shouldGenerateMainClassForRuntime(GeneratorContext generatorContext) {
        return generatorContext.getApplicationType() == ApplicationType.FUNCTION &&
                generatorContext.getFeatures().isFeaturePresent(AwsLambda.class);
    }

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        Features features = generatorContext.getFeatures();
        if (features.isFeaturePresent(AwsLambda.class)) {
            ApplicationType applicationType = generatorContext.getApplicationType();
            if (applicationType == ApplicationType.DEFAULT) {
                return AwsLambdaCustomRuntime.MAIN_CLASS_NAME;
            } else if (applicationType == ApplicationType.FUNCTION) {
                return generatorContext.getProject().getPackageName() + ".FunctionLambdaRuntime";
            }
        }
        throw new ConfigurationException("aws-lambda-custom-runtime should be used together with aws-lambda or aws-gateway-lambda-proxy");
    }

    private void addFunctionLambdaRuntime(GeneratorContext generatorContext, Project project) {
        String functionLambdaRuntime = generatorContext.getSourcePath("/{packagePath}/FunctionLambdaRuntime");
        generatorContext.addTemplate("functionLambdaRuntime", functionLambdaRuntime,
                functionLambdaRuntimeJava.template(generatorContext.getFeatures(), project),
                functionLambdaRuntimeKotlin.template(generatorContext.getFeatures(), project),
                functionLambdaRuntimeGroovy.template(generatorContext.getFeatures(), project));
    }

    @Override
    public String getCategory() {
        return Category.SERVERLESS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html";
    }
}
