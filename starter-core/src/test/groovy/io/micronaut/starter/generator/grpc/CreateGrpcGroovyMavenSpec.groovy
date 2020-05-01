package io.micronaut.starter.generator.grpc

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateGrpcGroovyMavenSpec extends CommandSpec {

    @Unroll
    void '#applicationType with features #features #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                        Language lang,
                                                                                                        BuildTool buildTool) {
        given:
        generateGrpcProject(lang, buildTool)

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
