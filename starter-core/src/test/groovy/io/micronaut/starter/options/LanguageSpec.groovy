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
        path = '/{packagePath}/{className}'
    }


}
