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
package io.micronaut.starter.feature.awsalexa;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerJava;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerGroovy;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerKotlin;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerJava;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerGroovy;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerKotlin;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerJava;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerGroovy;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerKotlin;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerJava;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerGroovy;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerKotlin;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerJava;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerGroovy;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerKotlin;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerJava;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerGroovy;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerKotlin;

import javax.inject.Singleton;

@Singleton
public class AwsAlexa implements Feature {

    @Override
    public String getName() {
        return "aws-alexa";
    }

    @Override
    public String getTitle() {
        return "Alexa Skill as Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for hosting a Custom Alexa Skill as an AWS Lambda function (https://developer.amazon.com/en-US/docs/alexa/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html).";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return (applicationType == ApplicationType.FUNCTION || applicationType == ApplicationType.DEFAULT);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        cancelIntentHandler(generatorContext, project);
        fallbackIntentHandler(generatorContext, project);
        helpIntentHandler(generatorContext, project);
        launchRequestIntentHandler(generatorContext, project);
        sessionEndedRequestIntentHandler(generatorContext, project);
        stopIntentHandler(generatorContext, project);
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

    @Override
    public String getCategory() {
        return Category.IOT;
    }
}
