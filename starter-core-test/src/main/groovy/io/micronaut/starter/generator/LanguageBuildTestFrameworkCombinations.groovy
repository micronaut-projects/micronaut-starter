package io.micronaut.starter.generator

import groovy.transform.AutoFinal
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFrameworkUtils

@AutoFinal
class LanguageBuildTestFrameworkCombinations {

    /**
     *
     * @return a List where each element is the list is a triple of [{@link Language}, {@link BuildTool}, {@link io.micronaut.starter.options.TestFramework}]
     */
    static List combinations() {
        List result = []
        for (Language language : Language.values()) {
            result.addAll([language, BuildTool.values().toList(), TestFrameworkUtils.supportedTestFrameworksByLanguage(language)].combinations())
        }
        result
    }
}
