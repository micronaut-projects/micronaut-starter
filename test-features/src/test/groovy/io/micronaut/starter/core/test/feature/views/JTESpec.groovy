package io.micronaut.starter.core.test.feature.views

import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import io.micronaut.starter.util.LanguageUtils
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

class JTESpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "jteViews"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven views-jte with #language"(Language language) {
        given:
        List<String> features = ["views-jte"]
        if (language == Language.KOTLIN) {
            features.add('kapt')
        }
        when:
        generateProject(language, BuildTool.MAVEN, features)
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << LanguageUtils.supportedLanguages(BuildTool.MAVEN)
    }

    @Unroll
    void "test gradle views-jte with #language and #dsl"(Language language, BuildTool buildTool, String dsl) {
        given:
        List<String> features = ["views-jte"]
        if (language == Language.KOTLIN) {
            features.add('kapt')
        }
        when:
        generateProject(language, buildTool, features)
        BuildResult result = executeGradle("build")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
        dsl = buildTool == BuildTool.GRADLE ? "Groovy DSL" : "Kotlin DSL"
    }
}
