package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture

class LogbackSpec extends ApplicationContextSpec  implements CommandOutputFixture {
    void 'by default jansi true'() {
        when:
        Map<String, String> output = generate([])
        String xml = output["src/main/resources/logback.xml"]

        then:
        xml
        xml.contains("<withJansi>true</withJansi>")
        xml.contains("<pattern>%cyan(%d{HH:mm:ss.SSS}) %gray([%thread]) %highlight(%-5level) %magenta(%logger{36}) - %msg%n</pattern>")
    }

    void 'with aws-lambda with jansi false since CloudWatch does not works with jansi'() {
        when:
        Map<String, String> output = generate([AwsLambda.FEATURE_NAME_AWS_LAMBDA])
        String xml = output["src/main/resources/logback.xml"]

        then:
        xml
        xml.contains("<withJansi>false</withJansi>")
        xml.contains("<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>")
    }
}
