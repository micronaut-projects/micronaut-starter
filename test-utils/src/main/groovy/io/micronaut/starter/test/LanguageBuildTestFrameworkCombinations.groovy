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
        features ? [Language.values(), BuildTool.values(), TestFramework.values(), features].combinations() : [Language.values(), BuildTool.values(), TestFramework.values()].combinations()
    }
}
