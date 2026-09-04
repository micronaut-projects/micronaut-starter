package io.micronaut.starter.options

import io.micronaut.starter.util.LanguageUtils
import spock.lang.Specification
import spock.lang.Unroll

class TestFrameworkSpec extends Specification {

    @Unroll("expected test source path: #expected for path: #path , lang: #lang and test framework: #testFramework")
    void "getSourcePath returns a path with the correct language extension and test framework suffix"(Language lang,
                                                                                                      TestFramework testFramework,
                                                                                                      String expected,
                                                                                                      String path) {
        expect:
        expected == testFramework.getSourcePath(path, lang)

        where:
        lang            | testFramework             || expected
        Language.JAVA   | TestFramework.JUNIT       || "src/test/java/{packagePath}/{className}Test.java"
        Language.GROOVY | TestFramework.JUNIT       || "src/test/groovy/{packagePath}/{className}Test.groovy"
        Language.KOTLIN | TestFramework.JUNIT       || "src/test/kotlin/{packagePath}/{className}Test.kt"
        Language.JAVA   | TestFramework.SPOCK       || "src/test/groovy/{packagePath}/{className}Spec.groovy"
        Language.KOTLIN | TestFramework.SPOCK       || "src/test/groovy/{packagePath}/{className}Spec.groovy"
        Language.GROOVY | TestFramework.SPOCK       || "src/test/groovy/{packagePath}/{className}Spec.groovy"
        Language.JAVA   | TestFramework.KOTEST  || "src/test/kotlin/{packagePath}/{className}Test.kt"
        Language.KOTLIN | TestFramework.KOTEST  || "src/test/kotlin/{packagePath}/{className}Test.kt"
        Language.GROOVY | TestFramework.KOTEST  || "src/test/kotlin/{packagePath}/{className}Test.kt"
        Language.PYTHON | TestFramework.PYTEST  || "tests/{packagePath}/{className}.py"
        path = '/{packagePath}/{className}'
    }

    @Unroll("getDefaultLanguage for test framework: #testFramework return #expected")
    void "verify the default language for a test framework"(Language expected, TestFramework testFramework) {
        expect:
        expected == testFramework.getDefaultLanguage()

        where:
        expected        | testFramework
        Language.JAVA   | TestFramework.JUNIT
        Language.GROOVY | TestFramework.SPOCK
        Language.KOTLIN | TestFramework.KOTEST
        Language.PYTHON | TestFramework.PYTEST
    }

    @Unroll("getSupportedLanguages for test framework: #testFramework return #expected")
    void "verify the list of supported languages for a test framework"(List<Language> expected, TestFramework testFramework) {
        given:
        expect:
        new ArrayList<>(expected).sort { a, b -> a.name <=> b.name } ==
                testFramework.supportedLanguages.sort { a, b -> a.name <=> b.name }

        where:
        expected                                          | testFramework
        LanguageUtils.JVM_LANGUAGES                       | TestFramework.JUNIT
        [Language.GROOVY]                                 | TestFramework.SPOCK
        [Language.KOTLIN]                                 | TestFramework.KOTEST
        [Language.PYTHON]                                 | TestFramework.PYTEST
    }
}
