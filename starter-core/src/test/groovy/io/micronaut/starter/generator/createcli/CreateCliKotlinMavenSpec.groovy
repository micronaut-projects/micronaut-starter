package io.micronaut.starter.generator.createcli

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class CreateCliKotlinMavenSpec extends CommandSpec {

    @Unroll
    void '#applicationType with features #features #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                        Language lang,
                                                                                                        BuildTool buildTool) {
        given:
        generateCliProject(new Options(lang, null, buildTool))

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run --args="-v"')
        } else {
            executeMavenCommand("mn:run -Dmn.appArgs=-v")
        }

        then:
        testOutputContains("Hi")

        where:
        applicationType     | lang            | buildTool
        ApplicationType.CLI | Language.KOTLIN | BuildTool.MAVEN
    }
}
