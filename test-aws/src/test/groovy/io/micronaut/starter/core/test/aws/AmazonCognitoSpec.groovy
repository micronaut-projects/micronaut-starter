package io.micronaut.starter.core.test.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class AmazonCognitoSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-amazon-cognito"
    }

    @Unroll
    void 'create-#applicationType with features amazon-cognito #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                             Language lang,
                                                                                                                             BuildTool build,
                                                                                                                             TestFramework testFramework) {
        given:
        List<String> features = ['amazon-cognito']
        if (build.isGradle()) {
            features.add('kapt')
        }
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT])
    }


    @Unroll
    void 'create-#applicationType with features amazon-cognito, aws-lambda #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                    Language lang,
                                                                                                                    BuildTool build,
                                                                                                                    TestFramework testFramework) {
        given:
        List<String> features = ['amazon-cognito', 'aws-lambda']
        if (build.isGradle()) {
            features.add('kapt')
        }
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT])
    }
}
