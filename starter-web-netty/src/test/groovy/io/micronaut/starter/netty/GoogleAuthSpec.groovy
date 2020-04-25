package io.micronaut.starter.netty

import io.micronaut.context.annotation.Property
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.netty.analytics.AnalyticsClient
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject
import java.util.concurrent.ExecutionException

@MicronautTest(environments = Environment.GOOGLE_COMPUTE)
@Property(
        name = "micronaut.http.services.analytics.url",
        value =  "http://localhost:8080/analytics/report")
@Property(
        name = "micronaut.http.services.analytics.token",
        value =  "xxxxxxx")
class GoogleAuthSpec extends Specification {

    @Inject AnalyticsClient client

    void "use google auth filter"() {
        when:
        client.applicationGenerated(new Generated(ApplicationType.CLI, Language.KOTLIN, BuildTool.MAVEN, TestFramework.SPOCK, JdkVersion.JDK_8)).get()

        then:
        def e = thrown(ExecutionException)
        e.cause.message.contains('metadata: nodename nor servname provided')
    }
}
