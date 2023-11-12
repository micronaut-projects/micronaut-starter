package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared

class LogbackSpec extends ApplicationContextSpec  implements CommandOutputFixture {
    @Shared
    Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, AwsLambdaFeatureValidator.firstSupportedJdk())

    void 'by default jansi false, coloring true, and jul false'() {
        when:
        Map<String, String> output = generate([])
        String xml = output["src/main/resources/logback.xml"]

        then:
        xml
        !xml.contains("<contextListener class=\"ch.qos.logback.classic.jul.LevelChangePropagator\"/>")
        !xml.contains("<withJansi>true</withJansi>")
        xml.contains("<pattern>%cyan(%d{HH:mm:ss.SSS}) %gray([%thread]) %highlight(%-5level) %magenta(%logger{36}) - %msg%n</pattern>")
        !xml.contains("<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>")
    }

    void 'with aws-lambda with jansi false since CloudWatch does not works with jansi'() {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, [AwsLambda.FEATURE_NAME_AWS_LAMBDA])
        String xml = output["src/main/resources/logback.xml"]

        then:
        xml
        !xml.contains("<contextListener class=\"ch.qos.logback.classic.jul.LevelChangePropagator\"/>")
        !xml.contains("<withJansi>true</withJansi>")
        !xml.contains("<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>")
        xml.contains("<pattern>%cyan(%d{HH:mm:ss.SSS}) %gray([%thread]) %highlight(%-5level) %magenta(%logger{36}) - %msg%n</pattern>")
    }
}
