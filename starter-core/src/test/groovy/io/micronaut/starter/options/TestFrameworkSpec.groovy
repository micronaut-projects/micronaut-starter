package io.micronaut.starter.options

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
        Language.JAVA   | TestFramework.KOTLINTEST  || "src/test/kotlin/{packagePath}/{className}Test.kt"
        Language.KOTLIN | TestFramework.KOTLINTEST  || "src/test/kotlin/{packagePath}/{className}Test.kt"
        Language.GROOVY | TestFramework.KOTLINTEST  || "src/test/kotlin/{packagePath}/{className}Test.kt"
        path = '/{packagePath}/{className}'
    }

}
