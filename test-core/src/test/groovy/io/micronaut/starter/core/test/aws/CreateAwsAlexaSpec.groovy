package io.micronaut.starter.core.test.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Retry
import spock.lang.Unroll

class CreateAwsAlexaSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "test-awsalexa"
    }

    @Retry // can fail on CI due to port binding race condition, so retry
    @Unroll
    void 'create-#applicationType with features aws-alexa #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                               Language lang,
                                                                                                               BuildTool build,
                                                                                                               TestFramework testFramework) {
        given:
        List<String> features = ['aws-alexa']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT, ApplicationType.FUNCTION])
    }
}
