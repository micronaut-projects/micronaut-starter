package io.micronaut.starter.generator

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateAppWithAwsApiGatewayLambdaProxSpec extends CommandSpec {

    @Unroll
    void 'create-app with features aws-api-gateway-proxy #lang and #build'(Language lang,
                                                                           BuildTool build,
                                                                           TestFramework testFramework) {
        given:
        generateDefaultProject(lang, build, ['aws-api-gateway-lambda-proxy'], testFramework)

        when:
        if (build == BuildTool.GRADLE) {
            executeGradleCommand('test')
        } else {
            executeMavenCommand("test")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        lang            | build             | testFramework
        Language.JAVA   | BuildTool.GRADLE  | null
        Language.JAVA   | BuildTool.GRADLE  | TestFramework.JUNIT
        Language.JAVA   | BuildTool.GRADLE  | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.GRADLE  | null
        Language.KOTLIN | BuildTool.GRADLE  | TestFramework.KOTLINTEST
        Language.KOTLIN | BuildTool.GRADLE  | TestFramework.JUNIT
        Language.GROOVY | BuildTool.GRADLE  | null
        Language.GROOVY | BuildTool.GRADLE  | TestFramework.SPOCK
        Language.JAVA   | BuildTool.MAVEN  | null
        Language.JAVA   | BuildTool.MAVEN  | TestFramework.JUNIT
        Language.JAVA   | BuildTool.MAVEN  | TestFramework.SPOCK
        Language.KOTLIN | BuildTool.MAVEN  | null
        Language.KOTLIN | BuildTool.MAVEN  | TestFramework.KOTLINTEST
        Language.KOTLIN | BuildTool.MAVEN  | TestFramework.JUNIT
        Language.GROOVY | BuildTool.MAVEN  | null
        Language.GROOVY | BuildTool.MAVEN  | TestFramework.SPOCK
    }
}
