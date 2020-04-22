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
package io.micronaut.starter.feature.function.gcp;

import com.fizzed.rocker.RockerModel;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.gcp.template.*;
import io.micronaut.starter.feature.server.template.groovyController;
import io.micronaut.starter.feature.server.template.javaController;
import io.micronaut.starter.feature.server.template.kotlinController;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

/**
 * A feature for supporting Google Cloud Function.
 *
 * @author graemerocher
 * @since 2.0.0
 */
@Singleton
public class GoogleCloudFunction implements FunctionFeature {

    public static final String NAME = "google-cloud-function";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Google Cloud Function Support";
    }

    @Override
    public String getDescription() {
        return "Adds Support for Google Cloud Function (https://cloud.google.com/functions)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationType type = generatorContext.getApplicationType();
        if (type == ApplicationType.DEFAULT) {

            Project project = generatorContext.getProject().withClassName("Hello");
            BuildTool buildTool = generatorContext.getBuildTool();
            generatorContext.addTemplate("readme", new RockerTemplate(
                    "README.md",
                    gcpFunctionReadme.template(project,
                            generatorContext.getFeatures(),
                            getRunCommand(buildTool),
                            getBuildCommand(buildTool)
                    )));
            Language language = generatorContext.getLanguage();
            String sourceFile = language.getSrcDir() + "/{packagePath}/HelloController." + language.getExtension();
            TestFramework testFramework = generatorContext.getTestFramework();
            String testSource =  testFramework.getFilename("/{packagePath}/HelloFunction", language);
            switch (language) {
                case GROOVY:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            groovyController.template(project)));
                    break;
                case KOTLIN:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            kotlinController.template(project)));
                    break;
                case JAVA:
                default:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            javaController.template(project)));
                    break;
            }


            RockerModel testTemplate;
            switch (testFramework) {
                case SPOCK:
                    testTemplate = gcpFunctionSpock.template(project);
                break;
                case KOTLINTEST:
                    testTemplate = gcpFunctionKotlinTest.template(project);
                break;
                case JUNIT:
                default:
                    switch (language) {
                        case GROOVY:
                            testTemplate = gcpFunctionGroovyJunit.template(project);
                            break;
                        case KOTLIN:
                            testTemplate = gcpFunctionKotlinJunit.template(project);
                            break;
                        case JAVA:
                        default:
                            testTemplate = gcpFunctionJavaJunit.template(project);
                            break;
                    }
                    break;
            }

            if (testTemplate != null) {
                generatorContext.addTemplate("testFunction", new RockerTemplate(
                        testSource,
                        testTemplate)
                );
            }

        }
    }

    private String getRunCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw function:run";
        } else {
            return "gradlew runFunction";
        }
    }

    private String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package";
        } else {
            return "gradlew clean shadowJar";
        }
    }
}
