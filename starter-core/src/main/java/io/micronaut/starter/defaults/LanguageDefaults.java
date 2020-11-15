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
package io.micronaut.starter.defaults;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.options.BuildTool;

/**
 * Default values to be applied when a given
 * {@link io.micronaut.starter.options.Language} is selected
 */
@Introspected
public class LanguageDefaults implements HasDefaultTest, HasDefaultBuild {

    TestFramework test;
    BuildTool build;

    @Creator
    public LanguageDefaults(TestFramework test, BuildTool build) {
        this.test = test;
        this.build = build;
    }

    public TestFramework getTest() {
        return test;
    }

    public BuildTool getBuild() {
        return build;
    }
}
