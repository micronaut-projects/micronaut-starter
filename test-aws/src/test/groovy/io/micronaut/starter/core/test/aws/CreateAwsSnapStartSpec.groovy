package io.micronaut.starter.core.test.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.aws.AmazonApiGateway
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class CreateAwsSnapStartSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-aws-snapstart"
    }

    @Unroll
    void 'create-#applicationType with features for aws SnapStart #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                Language lang,
                                                                                                                BuildTool build,
                                                                                                                TestFramework testFramework) {
        given:
        List<String> features = [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME]
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT, ApplicationType.FUNCTION])
    }

    @Unroll
    void 'create-#applicationType with graalvm does not enable SnapStart #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                Language lang,
                                                                                                                BuildTool build,
                                                                                                                TestFramework testFramework) {
        given:
        List<String> features = [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME, GraalVM.FEATURE_NAME_GRAALVM]
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations(
                [ApplicationType.DEFAULT, ApplicationType.FUNCTION],
                [Language.JAVA, Language.KOTLIN], // groovy not supported for GraalVM
                [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN] // maven not supported for GraalVM
        )
    }
}
