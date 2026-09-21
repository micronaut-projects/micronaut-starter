package io.micronaut.starter.options

import spock.lang.Specification
import spock.lang.Unroll

class LanguageSpec extends Specification {

    @Unroll("expected source path: #expected for path: #path , lang: #lang")
    void "getSourcePath returns a path with the correct language extension and source folder"(Language lang,
                                                                                              String expected,
                                                                                              String path) {
        expect:
        expected == lang.getSourcePath(path)

        where:
        lang            || expected
        Language.JAVA   || "src/main/java/{packagePath}/{className}.java"
        Language.GROOVY || "src/main/groovy/{packagePath}/{className}.groovy"
        Language.KOTLIN || "src/main/kotlin/{packagePath}/{className}.kt"
        Language.PYTHON || "src/{packagePath}/{className}.py"
        path = '/{packagePath}/{className}'
    }

    @Unroll("expected test source path: #expected for path: #path , lang: #lang")
    void "getTestSourcePath returns a path with the correct language extension and test source folder"(Language lang,
                                                                                                          String expected,
                                                                                                          String path) {
        expect:
        expected == lang.getTestSourcePath(path)

        where:
        lang            || expected
        Language.JAVA   || "src/test/java/{packagePath}/{className}.java"
        Language.GROOVY || "src/test/groovy/{packagePath}/{className}.groovy"
        Language.KOTLIN || "src/test/kotlin/{packagePath}/{className}.kt"
        Language.PYTHON || "tests/{packagePath}/{className}.py"
        path = '/{packagePath}/{className}'
    }

    void "Python uses Pytest and Pyronaut as defaults"() {
        expect:
        Language.PYTHON.getDefaults().getTest() == TestFramework.PYTEST
        Language.PYTHON.getDefaults().getBuild() == BuildTool.PYRONAUT
    }
}
