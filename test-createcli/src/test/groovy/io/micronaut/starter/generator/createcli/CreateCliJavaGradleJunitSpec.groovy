package io.micronaut.starter.generator.createcli

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateCliJavaGradleJunitSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "test-createcli-createclijavagradlejunitspec"
    }

    @Unroll
    void 'create-#applicationType with #lang and #buildTool and test framework: #testFramework'(ApplicationType applicationType,
                                                                                            Language lang,
                                                                                            BuildTool buildTool,
                                                                                            TestFramework testFramework) {

        given:
        generateProject(lang, buildTool, [], applicationType, testFramework)

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
        ApplicationType.CLI | Language.JAVA           | BuildTool.GRADLE   | TestFramework.JUNIT
    }
}
