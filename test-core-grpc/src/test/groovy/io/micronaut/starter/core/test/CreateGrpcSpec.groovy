package io.micronaut.starter.core.test

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
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
        generateProject(lang, buildTool, [], applicationType)

        when:
        if (buildTool.isGradle()) {
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
