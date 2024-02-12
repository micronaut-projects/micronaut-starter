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
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture

@MicronautTest
@Property(name = "spec.name", value =  "ReportAnalyticsSpec")
@Property(
        name = "micronaut.http.services.analytics.url",
        value =  "http://localhost:8080/analytics/report")
@Property(
        name = "micronaut.http.services.analytics.token",
        value =  "test-api-key")
class ReportAnalyticsSpec extends Specification {

    @Inject
    @Shared
    Environment environment

    @Inject
    @Shared
    EmbeddedServer embeddedServer

    @Inject
    ZipCreateControllerSpec.CreateClient client

    @Inject
    AnalyticsController controller

    def setupSpec() {
        environment.addPropertySource("test", [
                "micronaut.http.services.analytics.url": "$embeddedServer.URL/analytics/report"
        ])
    }

    void "test report analytics"() {
        when:
        client.createApp("test", Collections.emptyList(), BuildTool.MAVEN, null, null, null)
        PollingConditions conditions = new PollingConditions()

        then:
        conditions.eventually {
            controller.generated.buildTool == BuildTool.MAVEN
        }
    }

    @Requires(property = "spec.name", value =  "ReportAnalyticsSpec")
    @Controller('/')
    @Singleton
    static class AnalyticsController {
        Generated generated
        @Post("/analytics/report")
        CompletableFuture<HttpStatus> applicationGenerated(@Header xApiKey, @NonNull @Body Generated generated) {
            if (xApiKey != 'test-api-key') return CompletableFuture.completedFuture(HttpStatus.UNAUTHORIZED)
            this.generated = generated
            return CompletableFuture.completedFuture(HttpStatus.OK)
        }
    }

    @Requires(property = "spec.name", value =  "ReportAnalyticsSpec")
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
