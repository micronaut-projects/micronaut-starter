package io.micronaut.starter.core.test.feature.views

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class JTESpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "jteViews"
    }

    @Unroll
    void "test maven views-jte with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["views-jte"])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle views-jte with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["views-jte"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
