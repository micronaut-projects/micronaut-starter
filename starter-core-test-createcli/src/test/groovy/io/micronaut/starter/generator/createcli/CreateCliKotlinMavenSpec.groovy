package io.micronaut.starter.generator.createcli

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateCliKotlinMavenSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "starter-core-test-createcli-createclikotlinmavenspec"
    }

    @Unroll
    void '#applicationType with features #lang and #buildTool and test framework: #testFramework'(ApplicationType applicationType,
                                                                                              Language lang,
                                                                                              BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [], applicationType)

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
