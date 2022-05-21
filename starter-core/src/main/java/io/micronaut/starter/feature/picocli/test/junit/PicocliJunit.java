/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.feature.picocli.test.junit;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.picocli.test.PicocliTestFeature;
import io.micronaut.starter.options.AbstractJunitRockerModelProvider;
import io.micronaut.starter.options.JunitRockerModelProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

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

    public JunitRockerModelProvider getJunitRockerModelProvider(Project project) {
        return new AbstractJunitRockerModelProvider(project) {
            @Override
            public RockerModel javaJunit() {
                return picocliJunitTest.template(getProject());
            }

            @Override
            public RockerModel groovyJunit() {
                return picocliGroovyJunitTest.template(getProject());
            }

            @Override
            public RockerModel kotlinJunit() {
                return picocliKotlinJunitTest.template(getProject());
            }
        };
    }

    public RockerModel getModel(Language language, Project project) {
        JunitRockerModelProvider junitRockerModelProvider = getJunitRockerModelProvider(project);
        return junitRockerModelProvider.findJunitModel(language);
    }

    public RockerTemplate getTemplate(Language language, Project project) {
        String testSource =  getTestFramework().getSourcePath(PATH, language);
        return new RockerTemplate(testSource, getModel(language, project));
    }

}
