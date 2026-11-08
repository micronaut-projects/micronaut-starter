package io.micronaut.starter.netty

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import org.jspecify.annotations.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.options.BuildTool
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture

@MicronautTest
@Property(name = "spec.name", value =  "ReportAnalyticsSpec")
@Property(
        name = "micronaut.http.services.analytics.token",
        value =  "test-api-key")
class ReportAnalyticsSpec extends Specification implements TestPropertyProvider {

    // The server port must be known before the context starts: newer Micronaut Core versions
    // refuse to resolve the port of an embedded server bound to a random port before it has started.
    private static final int SERVER_PORT = findAvailablePort()

    @Inject
    ZipCreateControllerSpec.CreateClient client

    @Inject
    AnalyticsController controller

    @Override
    Map<String, String> getProperties() {
        [
                "micronaut.server.port"                : SERVER_PORT.toString(),
                "micronaut.http.services.analytics.url": "http://localhost:${SERVER_PORT}/analytics/report".toString()
        ]
    }

    private static int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.localPort
        }
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
}
