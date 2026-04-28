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

import groovy.transform.Memoized
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

import java.util.function.Function

class ApplicationTypeCombinations {
    static final Function<List, Boolean> SKIP_KOTLIN_MAVEN = l -> {
        !(l[1] == Language.KOTLIN && l[2] == BuildTool.MAVEN)
    }

    @Memoized
    static List combinations(List<ApplicationType> applicationTypes, List<Language> languages = Language.values() as List<Language>) {
        [applicationTypes, languages, BuildToolCombinations.buildTools, TestFrameworkCombinations.values()].combinations().findAll {
            SKIP_KOTLIN_MAVEN.apply(it)
        }.findAll {
            return LanguageBuildTestFrameworkCombinations.filterByTestFramework(it)
        }
    }

    @Memoized
    static List combinations(
            List<ApplicationType> applicationTypes,
            List<Language> languages,
            List<BuildTool> buildTools) {
        [applicationTypes, languages, buildTools, TestFrameworkCombinations.values()].combinations().findAll {
            SKIP_KOTLIN_MAVEN.apply(it)
        }.findAll {
            return LanguageBuildTestFrameworkCombinations.filterByTestFramework(it)
        }
    }
}
