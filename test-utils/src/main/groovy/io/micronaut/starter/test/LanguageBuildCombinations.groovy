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

import java.util.function.Function

@AutoFinal
class LanguageBuildCombinations {
    static final Function<List, Boolean> IS_KOTLIN_MAVEN = l -> {
        l[0] == Language.KOTLIN && l[1] == BuildTool.MAVEN
    }

    static final Function<List, Boolean> IS_PYRONAUT = l -> {
        l[0] == Language.PYTHON && l[1] == BuildTool.PYRONAUT
    }

    /**
     *
     * @return a List where each element is the list is a triple of [{@link io.micronaut.starter.options.Language}, {@link io.micronaut.starter.options.BuildTool}]
     */
    @Memoized
    static List<List> combinations(List<String> features = null) {
        (features
                ? [Language.values(), BuildToolCombinations.buildTools, features].combinations()
                : [Language.values(), BuildToolCombinations.buildTools].combinations()).findAll {
            !IS_KOTLIN_MAVEN.apply(it) && !IS_PYRONAUT.apply(it)
        }
    }

    static List<List> pythonCombinations(List<String> features = null) {
        features
                ? [Language.PYTHON, BuildTool.PYRONAUT, features].combinations()
                : [[Language.PYTHON, BuildTool.PYRONAUT]]
    }

    @Memoized
    static List<List> gradleCombinations(List<String> features = null) {
        (features ? [Language.values(), BuildTool.valuesGradle(), features].combinations() : [Language.values(), BuildTool.valuesGradle()].combinations()).findAll {
            !IS_PYRONAUT.apply(it)
        }
    }
}
