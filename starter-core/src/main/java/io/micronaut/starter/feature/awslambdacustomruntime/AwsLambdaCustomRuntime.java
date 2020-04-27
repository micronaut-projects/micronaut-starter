/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.awslambdacustomruntime;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.bookLambdaRuntimeJava;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.bookLambdaRuntimeKotlin;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.bookLambdaRuntimeGroovy;
import io.micronaut.starter.feature.awslambdacustomruntime.templates.bootstrap;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class AwsLambdaCustomRuntime implements FunctionFeature, ApplicationFeature {
    public static final String MAIN_CLASS_NAME = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime";

    public static final String FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME = "aws-lambda-custom-runtime";

    private final AwsLambda awsLambda;

    public AwsLambdaCustomRuntime(AwsLambda awsLambda) {
        this.awsLambda = awsLambda;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (awsLambda.supports(featureContext.getApplicationType()) && !featureContext.isPresent(AwsLambda.class)) {
            featureContext.addFeature(awsLambda);
        }
    }

    @Override
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
    public String getDescription() {
        return "Adds support for deploying a Micronaut Function to a Custom AWS Lambda Runtime";
    }

    @SuppressWarnings("EmptyBlock")
    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationFeature.super.apply(generatorContext);
        ApplicationType applicationType = generatorContext.getApplicationType();
        Project project = generatorContext.getProject();
        if (generatorContext.getFeatures().isFeaturePresent(AwsLambda.class)) {
            addBookLambdaRuntime(generatorContext, project);
        }
        addBootstrap(generatorContext, applicationType);
    }

    private void addBootstrap(GeneratorContext generatorContext, ApplicationType applicationType) {
        RockerModel bootstrapRockerModel = bootstrap.template(applicationType, generatorContext.getProject(), generatorContext.getBuildTool(), generatorContext.getFeatures());
        generatorContext.addTemplate("bootstrap", new RockerTemplate("bootstrap", bootstrapRockerModel));
    }

    @Override
    public String mainClassName(ApplicationType applicationType, Project project, Features features) {
        if (features.isFeaturePresent(AwsLambda.class)) {
            if (applicationType == ApplicationType.DEFAULT) {
                return AwsLambdaCustomRuntime.MAIN_CLASS_NAME;
            } else if (applicationType == ApplicationType.FUNCTION) {
                return project.getPackageName() + ".BookLambdaRuntime";
            }
        }
        throw new ConfigurationException("aws-lambda-custom-runtime should be used together with aws-lambda or aws-gateway-lambda-proxy");
    }

    private void addBookLambdaRuntime(GeneratorContext generatorContext, Project project) {
        String bookLambdaRuntime = generatorContext.getSourcePath("/{packagePath}/BookLambdaRuntime");
        generatorContext.addTemplate("bookLambdaRuntime", bookLambdaRuntime,
                bookLambdaRuntimeJava.template(project),
                bookLambdaRuntimeKotlin.template(project),
                bookLambdaRuntimeGroovy.template(project));
    }
}
