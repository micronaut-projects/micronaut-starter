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
package io.micronaut.starter.feature.awsapiproxy;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerGroovy;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerGroovyJunit;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerJava;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerJavaJunit;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerKotlin;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerKotlinJunit;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerKotlinTest;
import io.micronaut.starter.feature.awsapiproxy.template.bookControllerSpock;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedKotlin;

import javax.inject.Singleton;

@Singleton
public class AwsApiGatewayLambdaProxy implements FunctionFeature {

    public static final String FEATURE_NAME_AWS_API_GATEWAY_LAMBDA_PROXY = "aws-api-gateway-lambda-proxy";
    public static final String MAIN_CLASS_NAME = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime";

    @NonNull
    @Override
    public String getName() {
        return FEATURE_NAME_AWS_API_GATEWAY_LAMBDA_PROXY;
    }

    public String getTitle() {
        return "AWS API Gateway Lambda Proxy";
    }

    @Override
    public String getDescription() {
        return "Deploy your application to AWS Lambda and use API Gateway to proxy requests to it. Code your application with Controllers.";
    }

    // Even we are flagging this as a function feature (to avoid the inclusion of things such as http-client), we allow this function to be run only in DEFAULT
    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        addBook(generatorContext, project);
        addBookSaved(generatorContext, project);
        addBookController(generatorContext, project);
        addBookControllerTest(generatorContext, project);
    }

    private void addBookControllerTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/BookController");
        generatorContext.addTestTemplate("testBookController", testSource,
                bookControllerJavaJunit.template(project),
                bookControllerKotlinJunit.template(project),
                bookControllerGroovyJunit.template(project),
                bookControllerKotlinTest.template(project),
                bookControllerSpock.template(project));
    }

    private void addBookController(GeneratorContext generatorContext, Project project) {
        String bookControllerFile = generatorContext.getSourcePath("/{packagePath}/BookController");
        generatorContext.addTemplate("bookController", bookControllerFile,
                bookControllerJava.template(project),
                bookControllerKotlin.template(project),
                bookControllerGroovy.template(project));
    }

    private void addBook(GeneratorContext generatorContext, Project project) {
        String bookFile = generatorContext.getSourcePath("/{packagePath}/Book");
        generatorContext.addTemplate("book", bookFile,
                awsLambdaBookJava.template(project),
                awsLambdaBookKotlin.template(project),
                awsLambdaBookGroovy.template(project));
    }

    private void addBookSaved(GeneratorContext generatorContext, Project project) {
        String bookSavedFile = generatorContext.getSourcePath("/{packagePath}/BookSaved");
        generatorContext.addTemplate("bookSaved", bookSavedFile,
                awsLambdaBookSavedJava.template(project),
                awsLambdaBookSavedKotlin.template(project),
                awsLambdaBookSavedGroovy.template(project));
    }
}
