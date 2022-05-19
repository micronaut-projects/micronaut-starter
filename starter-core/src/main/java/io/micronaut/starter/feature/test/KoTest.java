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
package io.micronaut.starter.feature.test;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.URLTemplate;

import jakarta.inject.Singleton;

@Singleton
public class KoTest implements TestFeature {

    @Override
    public String getName() {
        return "kotest";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("koTestConfig",
                new URLTemplate("src/test/kotlin/io/kotest/provided/ProjectConfig.kt",
                        classLoader.getResource("kotest/ProjectConfig.kt")));
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.KOTEST;
    }

}
