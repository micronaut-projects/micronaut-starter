package io.micronaut.starter.test

import groovy.transform.AutoFinal
import groovy.transform.Memoized
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

@AutoFinal
class LanguageBuildCombinations {
    /**
     *
     * @return a List where each element is the list is a triple of [{@link io.micronaut.starter.options.Language}, {@link io.micronaut.starter.options.BuildTool}]
     */
    @Memoized
    static List combinations(List<String> features = null) {
        features ? [Language.values(), BuildToolCombinations.buildTools, features].combinations() : [Language.values(), BuildToolCombinations.buildTools].combinations()
    }
}
