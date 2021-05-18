/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.test

import groovy.transform.AutoFinal
import groovy.transform.Memoized
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

@AutoFinal
class LanguageBuildTestFrameworkCombinations {

    /**
     *
     * @return a List where each element is the list is a triple of [{@link Language}, {@link BuildTool}, {@link io.micronaut.starter.options.TestFramework}]
     */
    @Memoized
    static List combinations(List<String> features = null) {
        features ? [Language.values(), BuildToolCombinations.buildTools, TestFramework.values(), features].combinations() : [Language.values(), BuildToolCombinations.buildTools, TestFramework.values()].combinations()
    }
}
