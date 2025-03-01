package io.micronaut.starter.test

import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Specification
import spock.lang.Unroll

class LanguageBuildTestFrameworkCombinationsSpec extends Specification {

    @Unroll
    void "#language #buildTool #testFramework combination expected"(Language language, BuildTool buildTool, TestFramework testFramework) {
        expect:
        (buildTool == BuildTool.MAVEN && BuildToolTest.IGNORE_MAVEN) || LanguageBuildTestFrameworkCombinations.combinations().contains([language, buildTool, testFramework])

        where:
        language        | buildTool        | testFramework
        Language.JAVA   | BuildTool.GRADLE | TestFramework.JUNIT
        Language.GROOVY | BuildTool.GRADLE | TestFramework.JUNIT
        Language.KOTLIN | BuildTool.GRADLE | TestFramework.JUNIT
        Language.JAVA   | BuildTool.GRADLE | TestFramework.SPOCK
        Language.GROOVY | BuildTool.GRADLE | TestFramework.SPOCK
        Language.JAVA   | BuildTool.MAVEN  | TestFramework.JUNIT
        Language.GROOVY | BuildTool.MAVEN  | TestFramework.JUNIT
        Language.KOTLIN | BuildTool.MAVEN  | TestFramework.JUNIT
        Language.JAVA   | BuildTool.MAVEN  | TestFramework.SPOCK
        Language.GROOVY | BuildTool.MAVEN  | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.GRADLE | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.MAVEN  | TestFramework.SPOCK
        Language.JAVA   | BuildTool.GRADLE | TestFramework.KOTEST
        Language.KOTLIN | BuildTool.GRADLE | TestFramework.KOTEST
        Language.GROOVY | BuildTool.GRADLE | TestFramework.KOTEST
        Language.JAVA   | BuildTool.MAVEN  | TestFramework.KOTEST
        Language.KOTLIN | BuildTool.MAVEN  | TestFramework.KOTEST
        Language.GROOVY | BuildTool.MAVEN  | TestFramework.KOTEST
    }
}
