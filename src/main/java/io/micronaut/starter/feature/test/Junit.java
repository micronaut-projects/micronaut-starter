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

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;

@Singleton
public class Junit implements TestFeature {

    @Override
    public String getName() {
        return "junit";
    }

    @Override
    public void doApply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("testDir", new URLTemplate("src/test/" + commandContext.getLanguage().name() + "/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.junit;
    }

    @Override
    public Language getDefaultLanguage() {
        return Language.java;
    }

}
