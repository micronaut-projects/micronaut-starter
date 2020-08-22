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

import com.fizzed.rocker.RockerModel;
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

import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerGroovyJunit;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerGroovySpock;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerJavaJunit;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerKotlinJunit;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerKotlinTest;
import io.micronaut.starter.feature.awsalexa.templates.cancelIntentHandlerKoTest;

import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerGroovyJunit;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerGroovySpock;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerJavaJunit;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerKotlinJunit;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerKotlinTest;
import io.micronaut.starter.feature.awsalexa.templates.fallbackIntentHandlerKoTest;

import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerGroovyJunit;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerGroovySpock;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerJavaJunit;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerKotlinJunit;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerKotlinTest;
import io.micronaut.starter.feature.awsalexa.templates.helpIntentHandlerKoTest;

import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerGroovyJunit;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerGroovySpock;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerJavaJunit;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerKotlinJunit;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerKotlinTest;
import io.micronaut.starter.feature.awsalexa.templates.launchRequestIntentHandlerKoTest;

import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerGroovyJunit;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerGroovySpock;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerJavaJunit;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerKotlinJunit;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerKotlinTest;
import io.micronaut.starter.feature.awsalexa.templates.sessionEndedRequestIntentHandlerKoTest;

import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerGroovyJunit;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerGroovySpock;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerJavaJunit;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerKotlinJunit;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerKotlinTest;
import io.micronaut.starter.feature.awsalexa.templates.stopIntentHandlerKoTest;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.options.TestRockerModelProvider;

import javax.inject.Singleton;

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
        return "Adds support for building Alexa Skills with Micronaut.";
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
        TestRockerModelProvider testRockerModelProvider = new TestRockerModelProvider(project) {
            @Override
            public RockerModel spock() {
                return launchRequestIntentHandlerGroovySpock.template(getProject());
            }

            @Override
            public RockerModel kotlinTest() {
                return launchRequestIntentHandlerKotlinTest.template(getProject());
            }

            @Override
            public RockerModel koTest() {
                return launchRequestIntentHandlerKoTest.template(getProject());
            }

            @Override
            public RockerModel javaJunit() {
                return launchRequestIntentHandlerJavaJunit.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return launchRequestIntentHandlerGroovyJunit.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return launchRequestIntentHandlerKotlinJunit.template(getProject());
            }
        };
        generatorContext.addTemplate("testLaunchRequestIntentHandler", launchRequestIntentHandlerTest, testRockerModelProvider);
    }

    private void cancelIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String cancelIntentHandlerTest =  generatorContext.getTestSourcePath("/{packagePath}/CancelIntentHandler");
        TestRockerModelProvider testRockerModelProvider = new TestRockerModelProvider(project) {
            @Override
            public RockerModel spock() {
                return cancelIntentHandlerGroovySpock.template(getProject());
            }

            @Override
            public RockerModel kotlinTest() {
                return cancelIntentHandlerKotlinTest.template(getProject());
            }

            @Override
            public RockerModel koTest() {
                return cancelIntentHandlerKoTest.template(getProject());
            }

            @Override
            public RockerModel javaJunit() {
                return cancelIntentHandlerJavaJunit.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return cancelIntentHandlerGroovyJunit.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return cancelIntentHandlerKotlinJunit.template(getProject());
            }
        };
        generatorContext.addTemplate("testCancelIntentHandler", cancelIntentHandlerTest, testRockerModelProvider);
    }

    private void fallbackIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String fallbackIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/FallbackIntentHandler");
        TestRockerModelProvider testRockerModelProvider = new TestRockerModelProvider(project) {
            @Override
            public RockerModel spock() {
                return fallbackIntentHandlerGroovySpock.template(getProject());
            }

            @Override
            public RockerModel kotlinTest() {
                return fallbackIntentHandlerKotlinTest.template(getProject());
            }

            @Override
            public RockerModel koTest() {
                return fallbackIntentHandlerKoTest.template(getProject());
            }

            @Override
            public RockerModel javaJunit() {
                return fallbackIntentHandlerJavaJunit.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return fallbackIntentHandlerGroovyJunit.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return fallbackIntentHandlerKotlinJunit.template(getProject());
            }
        };
        generatorContext.addTemplate("testFallbackIntentHandler", fallbackIntentHandlerTest, testRockerModelProvider);
    }

    private void helpIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String helpIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/HelpIntentHandler");
        TestRockerModelProvider testRockerModelProvider = new TestRockerModelProvider(project) {
            @Override
            public RockerModel spock() {
                return helpIntentHandlerGroovySpock.template(getProject());
            }

            @Override
            public RockerModel kotlinTest() {
                return helpIntentHandlerKotlinTest.template(getProject());
            }

            @Override
            public RockerModel koTest() {
                return helpIntentHandlerKoTest.template(getProject());
            }

            @Override
            public RockerModel javaJunit() {
                return helpIntentHandlerJavaJunit.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return helpIntentHandlerGroovyJunit.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return helpIntentHandlerKotlinJunit.template(getProject());
            }
        };
        generatorContext.addTemplate("testHelpIntentHandler", helpIntentHandlerTest, testRockerModelProvider);

    }

    private void sessionEndedIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String sessionEndedIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/SessionEndedRequestIntentHandler");
        TestRockerModelProvider testRockerModelProvider = new TestRockerModelProvider(project) {
            @Override
            public RockerModel spock() {
                return sessionEndedRequestIntentHandlerGroovySpock.template(getProject());
            }

            @Override
            public RockerModel kotlinTest() {
                return sessionEndedRequestIntentHandlerKotlinTest.template(getProject());
            }

            @Override
            public RockerModel koTest() {
                return sessionEndedRequestIntentHandlerKoTest.template(getProject());
            }

            @Override
            public RockerModel javaJunit() {
                return sessionEndedRequestIntentHandlerJavaJunit.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return sessionEndedRequestIntentHandlerGroovyJunit.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return sessionEndedRequestIntentHandlerKotlinJunit.template(getProject());
            }
        };
        generatorContext.addTemplate("testSessionEndedRequestIntentHandler", sessionEndedIntentHandlerTest, testRockerModelProvider);

    }

    private void stopIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String stopIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/StopIntentHandler");
        TestRockerModelProvider testRockerModelProvider = new TestRockerModelProvider(project) {
            @Override
            public RockerModel javaJunit() {
                return stopIntentHandlerJavaJunit.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return stopIntentHandlerGroovyJunit.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return stopIntentHandlerKotlinJunit.template(getProject());
            }

            @Override
            public RockerModel spock() {
                return stopIntentHandlerGroovySpock.template(getProject());
            }

            @Override
            public RockerModel kotlinTest() {
                return stopIntentHandlerKotlinTest.template(getProject());
            }

            @Override
            public RockerModel koTest() {
                return stopIntentHandlerKoTest.template(getProject());
            }
        };

        generatorContext.addTemplate("testStopIntentHandler", stopIntentHandlerTest, testRockerModelProvider);
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
