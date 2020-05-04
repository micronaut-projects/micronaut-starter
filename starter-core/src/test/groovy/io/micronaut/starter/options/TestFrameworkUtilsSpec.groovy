package io.micronaut.starter.options

import spock.lang.Specification
import spock.lang.Unroll

class TestFrameworkUtilsSpec extends Specification {

    @Unroll
    void "#language supports tests frameworks: #description"(Language language, List<TestFramework> expected, String description) {
        expect:
        TestFrameworkUtils.supportedTestFrameworksByLanguage(language) == expected

        where:
        language        || expected
        Language.JAVA   || [TestFramework.JUNIT, TestFramework.SPOCK]
        Language.GROOVY || [TestFramework.JUNIT, TestFramework.SPOCK]
        Language.KOTLIN || [TestFramework.KOTLINTEST, TestFramework.JUNIT]as List<TestFramework>
        description = expected.collect { it.toString() }.join(',')

    }
}
