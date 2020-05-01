package io.micronaut.starter.generator.createcli

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateCliJavaGradleSpockSpec extends CommandSpec {

    @Unroll
    void 'create-#applicationType with features #features #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                               Language lang,
                                                                                                               BuildTool buildTool,
                                                                                                               TestFramework testFramework) {

        given:
        generateCliProject(new Options(lang, testFramework, buildTool))

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('test')
        } else {
            executeMavenCommand("compile test")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        applicationType     | lang                    | buildTool          | testFramework
        ApplicationType.CLI | Language.JAVA           | BuildTool.GRADLE   | TestFramework.SPOCK
    }
}
