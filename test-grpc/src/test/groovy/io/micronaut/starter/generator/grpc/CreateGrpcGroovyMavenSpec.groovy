package io.micronaut.starter.generator.grpc

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateGrpcGroovyMavenSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "test-grpc-creategrpcgroovymavenspec"
    }

    @Unroll
    void '#applicationType with #lang and #buildTool'(ApplicationType applicationType, Language lang, BuildTool buildTool) {
        given:
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
        applicationType      | lang                    | buildTool
        ApplicationType.GRPC | Language.GROOVY         | BuildTool.MAVEN
    }
}
