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

            Language language = generatorContext.getLanguage();
            switch (language) {
                case JAVA:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            "src/main/java/{packagePath}/HelloController.java",
                            javaController.template(project)));
                break;
                case GROOVY:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            "src/main/groovy/{packagePath}/HelloController.groovy",
                            groovyController.template(project)));
                    break;
                case KOTLIN:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            "src/main/kotlin/{packagePath}/HelloController.kt",
                            groovyController.template(project)));
                    break;
            }

            TestFramework testFramework = generatorContext.getTestFramework();
            String testFile = language.getTestSrcDir() + "/{packagePath}/HelloFunctionTest." + language.getExtension();
            RockerModel testTemplate = null;
            switch (testFramework) {
                case JUNIT:
                    switch (language) {
                        case JAVA:
                            testTemplate = gcpFunctionJavaJunit.template(project);
                        break;
                        case GROOVY:
                            testTemplate = gcpFunctionGroovyJunit.template(project);
                        break;
                        case KOTLIN:
                            testTemplate = gcpFunctionKotlinJunit.template(project);
                        break;
                    }
                break;
                case SPOCK:
                    testTemplate = gcpFunctionSpock.template(project);
                break;
                case KOTLINTEST:
                    testTemplate = gcpFunctionKotlinTest.template(project);
                break;
            }

            if (testTemplate != null) {
                generatorContext.addTemplate("testFunction", new RockerTemplate(
                        testFile,
                        testTemplate)
                );
            }

        }
    }
}
