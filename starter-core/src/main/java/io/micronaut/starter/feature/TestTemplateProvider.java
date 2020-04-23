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
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

public interface TestTemplateProvider {

    RockerModel javaJUnitTemplate(Project project);

    RockerModel kotlinJUnitTemplate(Project project);

    RockerModel groovyJUnitTemplate(Project project);

    RockerModel kotlinTestTemplate(Project project);

    RockerModel spockTemplate(Project project);

    default RockerModel getTestTemplate(Project project, GeneratorContext generatorContext) {
        TestFramework testFramework = generatorContext.getTestFramework();
        Language language = generatorContext.getLanguage();
        switch (testFramework) {
            case SPOCK:
                return spockTemplate(project);

            case KOTLINTEST:
                return kotlinTestTemplate(project);

            case JUNIT:
            default:
                switch (language) {
                    case GROOVY:
                        return groovyJUnitTemplate(project);
                    case KOTLIN:
                        return kotlinJUnitTemplate(project);
                    case JAVA:
                    default:
                        return javaJUnitTemplate(project);
                }
        }
    }
}
