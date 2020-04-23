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
package io.micronaut.starter.feature;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.template.RockerTemplate;

public final class FeatureUtils {

    private FeatureUtils() {

    }

    static RockerModel parseModel(Project project,
                                  GeneratorContext generatorContext,
                                  SourceTemplateProvider sourceTemplateProvider) {
        switch (generatorContext.getLanguage()) {
            case GROOVY:
                return sourceTemplateProvider.groovyTemplate(project);
            case KOTLIN:
                return sourceTemplateProvider.kotlinTemplate(project);
            case JAVA:
            default:
                return sourceTemplateProvider.javaTemplate(project);
        }
    }

    public static void addTestTemplate(Project project,
                                       GeneratorContext generatorContext,
                                       String templateName,
                                       String testSource,
                                       TestTemplateProvider testTemplateProvider) {
        RockerModel testTemplate = testTemplateProvider.getTestTemplate(project, generatorContext);
        if (testTemplate != null) {
            generatorContext.addTemplate(templateName, new RockerTemplate(testSource, testTemplate));
        }
    }

    public static void addTemplate(Project project,
                            GeneratorContext generatorContext,
                            String templateName,
                            String triggerFile,
                            SourceTemplateProvider sourceTemplateProvider) {
        RockerModel rockerModel = parseModel(project, generatorContext, sourceTemplateProvider);
        generatorContext.addTemplate(templateName, new RockerTemplate(triggerFile, rockerModel));
    }
}
