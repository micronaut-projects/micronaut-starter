package io.micronaut.starter.generator

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateAppWithAwsLambdaSpec extends CommandSpec {

    @Unroll
    void 'create-#applicationType with features aws-lambda #lang and #build and test framework: #testFramework'(Language lang,
                                                                BuildTool build,
                                                                ApplicationType applicationType,
                                                                TestFramework testFramework) {
        given:
        generateProject(lang, build, ['aws-lambda'], applicationType, testFramework)

        when:
        if (build == BuildTool.GRADLE) {
            executeGradleCommand('test')
        } else {
            executeMavenCommand("test")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        lang            | build             | applicationType         | testFramework
        Language.JAVA   | BuildTool.GRADLE  | ApplicationType.DEFAULT | null
        Language.JAVA   | BuildTool.GRADLE  | ApplicationType.DEFAULT | TestFramework.JUNIT
        Language.JAVA   | BuildTool.GRADLE  | ApplicationType.DEFAULT | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.GRADLE  | ApplicationType.DEFAULT | null
        Language.KOTLIN | BuildTool.GRADLE  | ApplicationType.DEFAULT | TestFramework.KOTLINTEST
        Language.KOTLIN | BuildTool.GRADLE  | ApplicationType.DEFAULT | TestFramework.JUNIT
        Language.GROOVY | BuildTool.GRADLE  | ApplicationType.DEFAULT | null
        Language.GROOVY | BuildTool.GRADLE  | ApplicationType.DEFAULT | TestFramework.SPOCK
        Language.JAVA   | BuildTool.MAVEN   | ApplicationType.DEFAULT | null
        Language.JAVA   | BuildTool.MAVEN   | ApplicationType.DEFAULT | TestFramework.JUNIT
        Language.JAVA   | BuildTool.MAVEN   | ApplicationType.DEFAULT | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.MAVEN   | ApplicationType.DEFAULT | null
        Language.KOTLIN | BuildTool.MAVEN   | ApplicationType.DEFAULT | TestFramework.KOTLINTEST
        Language.KOTLIN | BuildTool.MAVEN   | ApplicationType.DEFAULT | TestFramework.JUNIT
        Language.GROOVY | BuildTool.MAVEN   | ApplicationType.DEFAULT | null
        Language.GROOVY | BuildTool.MAVEN   | ApplicationType.DEFAULT | TestFramework.SPOCK
        Language.JAVA   | BuildTool.GRADLE  | ApplicationType.FUNCTION | null
        Language.JAVA   | BuildTool.GRADLE  | ApplicationType.FUNCTION | TestFramework.JUNIT
        Language.JAVA   | BuildTool.GRADLE  | ApplicationType.FUNCTION | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.GRADLE  | ApplicationType.FUNCTION | null
        Language.KOTLIN | BuildTool.GRADLE  | ApplicationType.FUNCTION | TestFramework.KOTLINTEST
        Language.KOTLIN | BuildTool.GRADLE  | ApplicationType.FUNCTION | TestFramework.JUNIT
        Language.GROOVY | BuildTool.GRADLE  | ApplicationType.FUNCTION | null
        Language.GROOVY | BuildTool.GRADLE  | ApplicationType.FUNCTION | TestFramework.SPOCK
        Language.JAVA   | BuildTool.MAVEN   | ApplicationType.FUNCTION | null
        Language.JAVA   | BuildTool.MAVEN   | ApplicationType.FUNCTION | TestFramework.JUNIT
        Language.JAVA   | BuildTool.MAVEN   | ApplicationType.FUNCTION | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.MAVEN   | ApplicationType.FUNCTION | null
        Language.KOTLIN | BuildTool.MAVEN   | ApplicationType.FUNCTION | TestFramework.KOTLINTEST
        Language.KOTLIN | BuildTool.MAVEN   | ApplicationType.FUNCTION | TestFramework.JUNIT
        Language.GROOVY | BuildTool.MAVEN   | ApplicationType.FUNCTION | null
        Language.GROOVY | BuildTool.MAVEN   | ApplicationType.FUNCTION | TestFramework.SPOCK
    }
}
