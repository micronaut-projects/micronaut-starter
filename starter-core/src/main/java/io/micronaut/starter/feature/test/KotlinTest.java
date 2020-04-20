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
package io.micronaut.starter.feature.test;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;

@Singleton
public class KotlinTest implements TestFeature {

    @Override
    public String getName() {
        return "kotlinTest";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("kotlinTestConfig",
                new URLTemplate("src/test/kotlin/io/kotlintest/provided/ProjectConfig.kt",
                        classLoader.getResource("kotlintest/ProjectConfig.kt")));
        generatorContext.addTemplate("testDir", new URLTemplate("src/test/kotlin/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.KOTLINTEST;
    }

    @Override
    public Language getDefaultLanguage() {
        return Language.KOTLIN;
    }

    @Override
    public String getTitle() {
        return "KotlinTest Testing Framework";
    }

    @Override
    public String getDescription() {
        return "Adds support for the KotlinTest testing framework";
    }
}
