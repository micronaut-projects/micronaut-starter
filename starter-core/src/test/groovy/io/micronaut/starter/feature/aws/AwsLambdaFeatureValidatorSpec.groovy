package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language

class AwsLambdaFeatureValidatorSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    static final List<String> AWS_LAMBDA_FEATURES = [
            "aws-lambda",
            "amazon-api-gateway",
            "amazon-api-gateway-http",
            "aws-lambda-s3-event-notification",
            "aws-lambda-scheduled-event",
            "aws-lambda-s3-event-notification"
    ]

    void 'test AWS Lambda feature validation does not fail for Java 17 and java runtime for feature=#feature'() {
        when:
        new BuildBuilder(beanContext, buildtool)
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .jdkVersion(JdkVersion.JDK_17)
                .features([feature])
                .render()

        then:
        noExceptionThrown()

        where:
        [buildtool, feature] << [BuildTool.values().toList(), AWS_LAMBDA_FEATURES].combinations()
    }

    void 'test AWS Lambda feature validation succeeds for Java 17 for feature=#feature with graalvm'() {
        when:
        new BuildBuilder(beanContext, buildtool)
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .jdkVersion(JdkVersion.JDK_17)
                .features([feature, "graalvm"])
                .render()

        then:
        noExceptionThrown()

        where:
        [buildtool, feature] << [BuildTool.values().toList(), AWS_LAMBDA_FEATURES].combinations()
    }

    void 'test AWS Lambda feature validation succeeds for jdk=#jdk and buildtool=#buildtool for feature=#feature'() {
        when:
        new BuildBuilder(beanContext, buildtool)
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .jdkVersion(jdk)
                .features([feature])
                .render()

        then:
        noExceptionThrown()

        where:
        [buildtool, feature, jdk] << [
                BuildTool.values().toList(),
                AWS_LAMBDA_FEATURES,
                AwsLambdaFeatureValidator.supportedJdks()
            ].combinations()
    }
}
