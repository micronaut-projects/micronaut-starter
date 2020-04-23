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
package io.micronaut.starter.feature.function.awslambda;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerGroovyJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerJavaJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerKotlinJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerKotlinTest;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookMicronautRequestHandlerSpock;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedKotlin;

import javax.inject.Singleton;

@Singleton
public class AwsLambda implements Feature {

    @Override
    public String getName() {
        return "aws-lambda";
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for writing functions to be deployed to AWS Lambda";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return (applicationType == ApplicationType.FUNCTION);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        addBook(generatorContext, project);
        addBookSaved(generatorContext, project);
        addBookMicronautRequestHandler(generatorContext, project);
        addTest(generatorContext, project);
    }

    private void addTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/BookMicronautRequestHandler");
        generatorContext.addTestTemplate("testBookMicronautRequestHandler", testSource,
                awsLambdaBookMicronautRequestHandlerJavaJunit.template(project),
                awsLambdaBookMicronautRequestHandlerKotlinJunit.template(project),
                awsLambdaBookMicronautRequestHandlerGroovyJunit.template(project),
                awsLambdaBookMicronautRequestHandlerKotlinTest.template(project),
                awsLambdaBookMicronautRequestHandlerSpock.template(project));
    }

    private void addBook(GeneratorContext generatorContext, Project project) {
        String bookFile = generatorContext.getSourcePath("/{packagePath}/Book");
        generatorContext.addTemplate("book", bookFile, awsLambdaBookJava.template(project), awsLambdaBookKotlin.template(project), awsLambdaBookGroovy.template(project));
    }

    private void addBookSaved(GeneratorContext generatorContext, Project project) {
        String bookSavedFile = generatorContext.getSourcePath("/{packagePath}/BookSaved");
        generatorContext.addTemplate("bookSaved", bookSavedFile, awsLambdaBookSavedJava.template(project),
                awsLambdaBookSavedKotlin.template(project), awsLambdaBookSavedGroovy.template(project));
    }

    private void addBookMicronautRequestHandler(GeneratorContext generatorContext, Project project) {
        String awsLambdaBookMicronautRequestHandlerFile = generatorContext.getSourcePath("/{packagePath}/BookMicronautRequestHandler");
        generatorContext.addTemplate("bookMicronautRequestHandler", awsLambdaBookMicronautRequestHandlerFile, awsLambdaBookMicronautRequestHandlerJava.template(project), awsLambdaBookMicronautRequestHandlerKotlin.template(project), awsLambdaBookMicronautRequestHandlerGroovy.template(project));
    }
}
