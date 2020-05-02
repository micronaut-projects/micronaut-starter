package io.micronaut.starter.generator

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateGrpcSpec extends CommandSpec {

    @Unroll
    void 'test basic create-grpc-app for #language and #buildTool'(Language language, BuildTool buildTool) {
        given:
        generateGrpcProject(language, buildTool)

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

}
