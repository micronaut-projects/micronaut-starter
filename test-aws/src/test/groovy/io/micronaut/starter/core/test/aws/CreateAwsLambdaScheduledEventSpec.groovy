package io.micronaut.starter.core.test.aws

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class CreateAwsLambdaScheduledEventSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-aws-lambda-scheduled-event"
    }

    @Unroll
    void 'create-#applicationType with features aws-lambda-scheduled-event #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                             Language lang,
                                                                                                                             BuildTool build,
                                                                                                                             TestFramework testFramework) {
        given:
        List<String> features = ['aws-lambda-scheduled-event']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.FUNCTION])
    }

    @Unroll
    void 'create-#applicationType with features aws-lambda-scheduled-event and aws-cdk #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                                Language lang,
                                                                                                                                BuildTool build,
                                                                                                                                TestFramework testFramework) {
        given:
        List<String> features = ['aws-lambda-scheduled-event', 'aws-cdk']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.FUNCTION])
    }
}
