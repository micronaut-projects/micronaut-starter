package io.micronaut.starter.generator

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateAppSpec extends CommandSpec {

    @Unroll
    void 'test basic create-app for #lang and #build'(Language lang, BuildTool build) {
        given:
        generateDefaultProject(lang, build)

        when:
        if (build == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        [lang, build] << [Language.values(), BuildTool.values()].combinations()
    }

}
