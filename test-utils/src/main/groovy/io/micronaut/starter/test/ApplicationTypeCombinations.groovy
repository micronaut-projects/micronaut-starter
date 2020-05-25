package io.micronaut.starter.test

import groovy.transform.Memoized
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class ApplicationTypeCombinations {
    @Memoized
    static List combinations(List<ApplicationType> applicationTypes, List<Language> languages = Language.values() as List<Language>) {
        [applicationTypes, languages, BuildToolCombinations.buildTools, TestFramework.values()].combinations()
                .findAll { !(it[1] == Language.GROOVY && it[2] == BuildTool.MAVEN) }
    }
}
