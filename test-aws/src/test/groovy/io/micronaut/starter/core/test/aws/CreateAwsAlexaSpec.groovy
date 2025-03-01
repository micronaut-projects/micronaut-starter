package io.micronaut.starter.core.test.aws

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Retry
import spock.lang.Unroll

@Retry // can fail on CI due to port binding race condition, so retry
class CreateAwsAlexaSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "test-awsalexa"
    }

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
