package io.micronaut.starter.netty

import io.micronaut.context.annotation.Property
import io.micronaut.starter.options.BuildTool
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
@Property(name = "spec.name", value = "ReportAnalyticsNoApiKeySpec")
@Property(
        name = "micronaut.http.services.analytics.url",
        value = "http://localhost:8080/analytics/report")
class ReportAnalyticsNoApiKeySpec extends Specification {

    @Inject
    ZipCreateControllerSpec.CreateClient client

    void "test report analytics with no analytics api key configured"() {
        when: "no analytics api key is configured"
        client.createApp("test", Collections.emptyList(), BuildTool.GRADLE, null, null)

        then: "create endpoint completes successfully (analytics are not invoked)"
        noExceptionThrown()
    }
}
