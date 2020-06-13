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
package io.micronaut.starter.feature.picocli.test.junit;

import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.picocli.test.PicocliTestFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class PicocliJunit implements PicocliTestFeature {

    @Override
    public String getName() {
        return "picocli-junit";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("picocliJunitTest", getTemplate(generatorContext.getLanguage(), generatorContext.getProject()));
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.JUNIT;
    }

    @Override
    public List<Language> getDefaultLanguages() {
        return Arrays.asList(Language.JAVA, Language.KOTLIN);
    }

    public RockerTemplate getTemplate(Language language, Project project) {

        String testSource =  getTestFramework().getSourcePath("/{packagePath}/{className}Command", language);

        if (language == Language.JAVA) {
            return new RockerTemplate(testSource, picocliJunitTest.template(project));
        } else if (language == Language.GROOVY) {
            return new RockerTemplate(testSource, picocliGroovyJunitTest.template(project));
        } else if (language == Language.KOTLIN) {
            return new RockerTemplate(testSource, picocliKotlinJunitTest.template(project));
        } else {
            return null;
        }
    }

}
