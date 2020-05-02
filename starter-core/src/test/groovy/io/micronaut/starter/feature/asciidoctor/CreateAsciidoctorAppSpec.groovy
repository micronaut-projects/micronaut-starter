package io.micronaut.starter.feature.asciidoctor

import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateAsciidoctorAppSpec extends CommandSpec implements CommandFixture {

    @Unroll
    void 'test create-app for asciidoctor feature with #language and #buildTool'(Language language, BuildTool buildTool) {
        given:
        generateProject(language, buildTool, ['asciidoctor'])

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('asciidoctor')
        } else {
            executeMavenCommand("generate-resources")
        }

        then:
        testOutputContains('BUILD SUCCESS')

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

}
