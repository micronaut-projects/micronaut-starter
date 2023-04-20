package io.micronaut.starter.core.test.feature.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.aws.AwsLambdaS3EventNotification
import io.micronaut.starter.feature.aws.AwsLambdaScheduledEvent
import io.micronaut.starter.feature.aws.AwsV2Sdk
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

class CustomLambdaRuntimeSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "lambda-custom-runtime"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven aws-lambda-custom-runtime#desc with #language"(Language language, String feature) {
        when:
        generateProject(language, BuildTool.MAVEN, [AwsV2Sdk.NAME, AwsLambdaCustomRuntime.FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME, feature], ApplicationType.FUNCTION)
        String output = executeMaven("compile")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [language, feature] << [supportedLanguages(), [AwsV2Sdk.NAME, AwsLambdaS3EventNotification.NAME, AwsLambdaScheduledEvent.NAME]].combinations()
        desc = feature == AwsV2Sdk.NAME ? "" : " and $feature"
    }

    @Unroll
    void "test gradle aws-lambda-custom-runtime#desc with #language"(Language language, String feature) {
        when:
        generateProject(language, BuildTool.GRADLE, [AwsLambdaCustomRuntime.FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME, feature], ApplicationType.FUNCTION)
        BuildResult result = executeGradle("classes")

        then:
        result.output.contains("BUILD SUCCESS")

        where:
        [language, feature] << [supportedLanguages(), [AwsV2Sdk.NAME, AwsLambdaS3EventNotification.NAME, AwsLambdaScheduledEvent.NAME]].combinations()
        desc = feature == AwsV2Sdk.NAME ? "" : " and $feature"
    }

    private static List<Language> supportedLanguages() {
        [
                Language.JAVA,
                Language.KOTLIN,
                //Language.GROOVY,
        ]
    }
}

