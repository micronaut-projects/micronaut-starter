package io.micronaut.starter.generator.app

import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.generator.LanguageBuildCombinations
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateAppSpec extends CommandSpec {

    @Unroll
    void 'test basic create-app for #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, ['random-port'])

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app"
    }
}
