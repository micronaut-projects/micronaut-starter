package io.micronaut.starter.core.test.create

import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Unroll

class CreateAsciidoctorSpec extends CommandSpec {

    @Unroll
    void 'test create-app for asciidoctor feature with #language and #buildTool'(Language language, BuildTool buildTool) {
        given:
        generateProject(language, buildTool, ['asciidoctor'])

        when:
        String output = null
        if (buildTool.isGradle()) {
            output = executeGradle('asciidoctor').output
        } else {
            output = executeMaven("generate-resources")
        }

        then:
        output?.contains('BUILD SUCCESS')

        where:
        [language, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-asciidoctor"
    }
}
