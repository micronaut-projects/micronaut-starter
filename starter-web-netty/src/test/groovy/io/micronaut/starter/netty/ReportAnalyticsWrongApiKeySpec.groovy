package io.micronaut.starter.netty

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.ServiceHttpClientConfiguration
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.options.BuildTool
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.util.concurrent.CompletableFuture

@MicronautTest
@Property(name = "spec.name", value = "ReportAnalyticsWrongApiKeySpec")
@Property(
        name = "micronaut.http.services.analytics.url",
        value =  "http://localhost:8080/analytics/report")
@Property(
        name = "micronaut.http.services.analytics.token",
        value =  "wrong-api-key")
class ReportAnalyticsWrongApiKeySpec extends Specification {

    @Inject
    @Shared
    Environment environment

    @Inject
    @Shared
    EmbeddedServer embeddedServer

    @Inject
    ZipCreateControllerSpec.CreateClient client

    @Inject
    UnauthorizedAnalyticsController controller

    def setupSpec() {
        environment.addPropertySource("test", [
                "micronaut.http.services.analytics.url": "$embeddedServer.URL/analytics/report"
        ])
    }

    void "test report analytics with the wrong analytics api key configured"() {
        when: "the wrong analytics api key is configured"
        client.createApp("test", Collections.emptyList(), BuildTool.GRADLE_KOTLIN, null, null)
        PollingConditions conditions = new PollingConditions()

        then: "analytics endpoint is hit and comes back as UNAUTHORIZED"
        conditions.eventually {
            controller.hit
        }

        and: "create endpoint completes successfully (analytics were invoked asynchronously, as an app event)"
        noExceptionThrown()
    }

    @Requires(property = "spec.name", value =  "ReportAnalyticsWrongApiKeySpec")
    @Controller('/')
    @Singleton
    static class UnauthorizedAnalyticsController {
        boolean hit
        @Post("/analytics/report")
        CompletableFuture<HttpStatus> applicationGenerated(@Header xApiKey, @NonNull @Body Generated generated) {
            this.hit = true
            return CompletableFuture.completedFuture(HttpStatus.UNAUTHORIZED)
        }
    }

    @Requires(property = "spec.name", value =  "ReportAnalyticsWrongApiKeySpec")
    @Singleton
    static class ServiceConfigurer implements BeanCreatedEventListener<ServiceHttpClientConfiguration> {

        final Provider<EmbeddedServer> embeddedServer

        ServiceConfigurer(Provider<EmbeddedServer> embeddedServer) {
            this.embeddedServer = embeddedServer
        }

        @Override
        ServiceHttpClientConfiguration onCreated(BeanCreatedEvent<ServiceHttpClientConfiguration> event) {
            def config = event.getBean()
            if (config.serviceId == 'analytics') {
                config.setUrl(embeddedServer.get().URI)
            }
            return config
        }
    }
}
