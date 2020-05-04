package io.micronaut.starter.generator.grpc

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.generator.LanguageBuildCombinations
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateGrpcSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "test-grpc"
    }

    @Unroll
    void 'grpc with #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        ApplicationType applicationType = ApplicationType.GRPC
        generateProject(lang, buildTool, [RandomGrpcPort.NAME], applicationType)

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
}
