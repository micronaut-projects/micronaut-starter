package io.micronaut.starter.core.test.feature.views

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

class ThymeleafSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "thymeleafViews"
    }

    @Unroll
    void "test gradle views-thymeleaf runs fieldset suite"(Language language, BuildTool buildTool, String dsl) {
        when:
        generateProject(language, buildTool, ["views-thymeleaf"])
        BuildResult result = executeGradle("build")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << [[Language.JAVA, BuildTool.GRADLE]]
        dsl = buildTool == BuildTool.GRADLE ? "Groovy DSL" : "Kotlin DSL"
    }
}
