package io.micronaut.starter.feature.logging

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Shared

class LogbackSpec extends ApplicationContextSpec  implements CommandOutputFixture {
    @Shared
    Options options = MicronautOptions.builder()
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .buildTool(BuildTool.GRADLE)
            .javaVersion(AwsLambdaFeatureValidator.firstSupportedJdk())
            .build()

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
