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
package io.micronaut.starter.feature.awsalexa;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.awsalexa.templates.*;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.TestRockerModelProvider;

import jakarta.inject.Singleton;

@Singleton
public class AwsAlexa implements Feature, CloudFeature {

    @Override
    public String getName() {
        return "aws-alexa";
    }

    @Override
    public String getTitle() {
        return "Alexa Skills";
    }

    @Override
    public String getDescription() {
        return "Build Alexa Skills with Micronaut";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return (applicationType == ApplicationType.FUNCTION || applicationType == ApplicationType.DEFAULT);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();

        cancelIntentHandler(generatorContext, project);
        cancelIntentHandlerTest(generatorContext, project);

        fallbackIntentHandler(generatorContext, project);
        fallbackIntentHandlerTest(generatorContext, project);

        helpIntentHandler(generatorContext, project);
        helpIntentHandlerTest(generatorContext, project);

        launchRequestIntentHandler(generatorContext, project);
        launchRequestIntentHandlerTest(generatorContext, project);

        sessionEndedRequestIntentHandler(generatorContext, project);
        sessionEndedIntentHandlerTest(generatorContext, project);

        stopIntentHandler(generatorContext, project);
        stopIntentHandlerTest(generatorContext, project);

        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.micronaut.aws")
                    .artifactId("micronaut-function-aws-alexa")
                    .compile());
        }
        if (generatorContext.getApplicationType() == ApplicationType.DEFAULT) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.micronaut.aws")
                    .artifactId("micronaut-aws-alexa-httpserver")
                    .compile());
        }
    }

    private void cancelIntentHandler(GeneratorContext generatorContext, Project project) {
        String cancelIntentHandler = generatorContext.getSourcePath("/{packagePath}/CancelIntentHandler");
        generatorContext.addTemplate("cancelIntentHandler", cancelIntentHandler,
                cancelIntentHandlerJava.template(project),
                cancelIntentHandlerKotlin.template(project),
                cancelIntentHandlerGroovy.template(project));
    }

    private void fallbackIntentHandler(GeneratorContext generatorContext, Project project) {
        String fallbackIntentHandler = generatorContext.getSourcePath("/{packagePath}/FallbackIntentHandler");
        generatorContext.addTemplate("fallbackIntentHandler", fallbackIntentHandler,
                fallbackIntentHandlerJava.template(project),
                fallbackIntentHandlerKotlin.template(project),
                fallbackIntentHandlerGroovy.template(project));
    }

    private void helpIntentHandler(GeneratorContext generatorContext, Project project) {
        String helpIntentHandler = generatorContext.getSourcePath("/{packagePath}/HelpIntentHandler");
        generatorContext.addTemplate("helpIntentHandler", helpIntentHandler,
                helpIntentHandlerJava.template(project),
                helpIntentHandlerKotlin.template(project),
                helpIntentHandlerGroovy.template(project));
    }

    private void launchRequestIntentHandler(GeneratorContext generatorContext, Project project) {
        String launchRequestIntentHandler = generatorContext.getSourcePath("/{packagePath}/LaunchRequestIntentHandler");
        generatorContext.addTemplate("launchRequestIntentHandler", launchRequestIntentHandler,
                launchRequestIntentHandlerJava.template(project),
                launchRequestIntentHandlerKotlin.template(project),
                launchRequestIntentHandlerGroovy.template(project));
    }

    private void sessionEndedRequestIntentHandler(GeneratorContext generatorContext, Project project) {
        String sessionEndedRequestIntentHandler = generatorContext.getSourcePath("/{packagePath}/SessionEndedRequestIntentHandler");
        generatorContext.addTemplate("sessionEndedRequestIntentHandler", sessionEndedRequestIntentHandler,
                sessionEndedRequestIntentHandlerJava.template(project),
                sessionEndedRequestIntentHandlerKotlin.template(project),
                sessionEndedRequestIntentHandlerGroovy.template(project));
    }

    private void stopIntentHandler(GeneratorContext generatorContext, Project project) {
        String stopIntentHandler = generatorContext.getSourcePath("/{packagePath}/StopIntentHandler");
        generatorContext.addTemplate("stopIntentHandler", stopIntentHandler,
                stopIntentHandlerJava.template(project),
                stopIntentHandlerKotlin.template(project),
                stopIntentHandlerGroovy.template(project));
    }

    private void launchRequestIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String launchRequestIntentHandlerTest =  generatorContext.getTestSourcePath("/{packagePath}/LaunchRequestIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(launchRequestIntentHandlerGroovySpock.template(project),
                launchRequestIntentHandlerJavaJunit.template(project),
                launchRequestIntentHandlerGroovyJunit.template(project),
                launchRequestIntentHandlerKotlinJunit.template(project),
                launchRequestIntentHandlerKoTest.template(project));
        generatorContext.addTemplate("testLaunchRequestIntentHandler", launchRequestIntentHandlerTest, provider);
    }

    private void cancelIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String cancelIntentHandlerTest =  generatorContext.getTestSourcePath("/{packagePath}/CancelIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(cancelIntentHandlerGroovySpock.template(project),
                cancelIntentHandlerJavaJunit.template(project),
                cancelIntentHandlerGroovyJunit.template(project),
                cancelIntentHandlerKotlinJunit.template(project),
                cancelIntentHandlerKoTest.template(project));
        generatorContext.addTemplate("testCancelIntentHandler", cancelIntentHandlerTest, provider);
    }

    private void fallbackIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String fallbackIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/FallbackIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(fallbackIntentHandlerGroovySpock.template(project),
                fallbackIntentHandlerJavaJunit.template(project),
                fallbackIntentHandlerGroovyJunit.template(project),
                fallbackIntentHandlerKotlinJunit.template(project),
                fallbackIntentHandlerKoTest.template(project));
        generatorContext.addTemplate("testFallbackIntentHandler", fallbackIntentHandlerTest, provider);
    }

    private void helpIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String helpIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/HelpIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(helpIntentHandlerGroovySpock.template(project),
                helpIntentHandlerJavaJunit.template(project),
                helpIntentHandlerGroovyJunit.template(project),
                helpIntentHandlerKotlinJunit.template(project),
                helpIntentHandlerKoTest.template(project));

        generatorContext.addTemplate("testHelpIntentHandler", helpIntentHandlerTest, provider);

    }

    private void sessionEndedIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String sessionEndedIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/SessionEndedRequestIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(sessionEndedRequestIntentHandlerGroovySpock.template(project),
                sessionEndedRequestIntentHandlerJavaJunit.template(project),
                sessionEndedRequestIntentHandlerGroovyJunit.template(project),
                sessionEndedRequestIntentHandlerKotlinJunit.template(project),
                sessionEndedRequestIntentHandlerKoTest.template(project));
        generatorContext.addTemplate("testSessionEndedRequestIntentHandler", sessionEndedIntentHandlerTest, provider);

    }

    private void stopIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String stopIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/StopIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(stopIntentHandlerGroovySpock.template(project),
                stopIntentHandlerJavaJunit.template(project),
                stopIntentHandlerGroovyJunit.template(project),
                stopIntentHandlerKotlinJunit.template(project),
                stopIntentHandlerKoTest.template(project));

        generatorContext.addTemplate("testStopIntentHandler", stopIntentHandlerTest, provider);
    }

    @Override
    public String getCategory() {
        return Category.IOT;
    }

    @Override
    public Cloud getCloud() {
        return Cloud.AWS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#alexa";
    }
}
