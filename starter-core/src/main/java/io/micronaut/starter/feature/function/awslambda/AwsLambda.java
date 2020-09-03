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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.awsalexa.AwsAlexa;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.DocumentationLink;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerGroovy;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerGroovyJunit;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerJava;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerJavaJunit;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerKotlin;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerKotlinJunit;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerKoTest;
import io.micronaut.starter.feature.function.awslambda.template.bookControllerSpock;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerGroovyJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerJavaJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerKotlinJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerKoTest;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerSpock;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedKotlin;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerWritable;

import javax.inject.Singleton;
import java.util.Set;

import static io.micronaut.starter.application.ApplicationType.*;

@Singleton
public class AwsLambda implements FunctionFeature, DefaultFeature, CloudFeature, HandlerClassFeature {

    public static final String FEATURE_NAME_AWS_LAMBDA = "aws-lambda";
    private static final String BOOK_REQUEST_HANDLER = "BookRequestHandler";
    private static final String MICRONAUT_LAMBDA_HANDLER = "io.micronaut.function.aws.proxy.MicronautLambdaHandler";
    private static final String LINK_TITLE = "AWS Lambda Handler";
    private static final String LINK_URL = "https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html";

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
        return "Adds support for writing functions to be deployed to AWS Lambda";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        if (shouldGenerateSources(generatorContext)) {
            addBook(generatorContext, project);
            addBookSaved(generatorContext, project);
            ApplicationType applicationType = generatorContext.getApplicationType();
            if (applicationType == DEFAULT || applicationType == FUNCTION) {
                if (applicationType == DEFAULT) {
                    addBookController(generatorContext, project);
                    addBookControllerTest(generatorContext, project);
                } else {
                    addRequestHandler(generatorContext, project);
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

    private void addBookControllerTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/BookController");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(bookControllerSpock.template(project),
                bookControllerJavaJunit.template(project),
                bookControllerGroovyJunit.template(project),
                bookControllerKotlinJunit.template(project),
                bookControllerKoTest.template(project));
        generatorContext.addTemplate("testBookController", testSource, provider);
    }

    private void addBookController(GeneratorContext generatorContext, Project project) {
        String bookControllerFile = generatorContext.getSourcePath("/{packagePath}/BookController");
        generatorContext.addTemplate("bookController", bookControllerFile,
                bookControllerJava.template(project),
                bookControllerKotlin.template(project),
                bookControllerGroovy.template(project));
    }

    private void addTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/BookRequestHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(awsLambdaBookRequestHandlerSpock.template(project),
                awsLambdaBookRequestHandlerJavaJunit.template(project),
                awsLambdaBookRequestHandlerGroovyJunit.template(project),
                awsLambdaBookRequestHandlerKotlinJunit.template(project),
                awsLambdaBookRequestHandlerKoTest.template(project));
        generatorContext.addTemplate("testBookRequestHandler", testSource, provider);
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

    private void addRequestHandler(GeneratorContext generatorContext, Project project) {
        String awsLambdaBookRequestHandlerFile = generatorContext.getSourcePath("/{packagePath}/" + BOOK_REQUEST_HANDLER);
        generatorContext.addTemplate("bookMicronautRequestHandler", awsLambdaBookRequestHandlerFile, awsLambdaBookRequestHandlerJava.template(project), awsLambdaBookRequestHandlerKotlin.template(project), awsLambdaBookRequestHandlerGroovy.template(project));
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
                return project.getPackageName() + "." + BOOK_REQUEST_HANDLER;
            default:
                return null;
        }
    }
}
