package io.micronaut.starter.util

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Specification
import spock.lang.Unroll

class LanguageUtilsSpec extends Specification {

    @Unroll
    void "supportedLanguages returns #languages for #buildTool"(BuildTool buildTool, List<Language> languages) {
        expect:
        LanguageUtils.supportedLanguages(buildTool) == languages

        where:
        buildTool               || languages
        BuildTool.GRADLE        || LanguageUtils.JVM_LANGUAGES
        BuildTool.GRADLE_KOTLIN || LanguageUtils.JVM_LANGUAGES
        BuildTool.MAVEN         || [Language.JAVA, Language.GROOVY]
        BuildTool.PYRONAUT      || [Language.PYTHON]
    }
}
