package io.micronaut.starter.netty

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.options.BuildTool
import jakarta.inject.Singleton
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class ReportAnalyticsWrongApiKeySpec extends Specification {
    void "test report analytics with the wrong analytics api key configured"() {
        given:
        EmbeddedServer starterAnalytics = ApplicationContext.run(EmbeddedServer, [
                'spec.name': 'ReportAnalyticsWrongApiKeySpecAnalytics'
        ])
        EmbeddedServer starterApi = ApplicationContext.run(EmbeddedServer, [
                "micronaut.http.services.analytics.token": "wrong-api-key",
                "micronaut.http.services.analytics.url": "http://localhost:${starterAnalytics.port}"
        ])
        ZipCreateControllerSpec.CreateClient client = starterApi.applicationContext.getBean(ZipCreateControllerSpec.CreateClient)

        when: "the wrong analytics api key is configured"
        UnauthorizedAnalyticsController controller = starterAnalytics.applicationContext.getBean(UnauthorizedAnalyticsController)
        client.createApp("test", Collections.emptyList(), BuildTool.GRADLE_KOTLIN, null, null, null)
        PollingConditions conditions = new PollingConditions()

        then: "analytics endpoint is hit and comes back as UNAUTHORIZED"
        conditions.eventually {
            controller.hit
        }

        and: "create endpoint completes successfully (analytics were invoked asynchronously, as an app event)"
        noExceptionThrown()

        cleanup:
        starterAnalytics.close()
        starterApi.close()
    }

    @Requires(property = "spec.name", value =  "ReportAnalyticsWrongApiKeySpecAnalytics")
    @Controller('/')
    @Singleton
    static class UnauthorizedAnalyticsController {
        boolean hit
        @Post("/analytics/report")
        @Status(HttpStatus.UNAUTHORIZED)
        void applicationGenerated(@Header xApiKey, @NonNull @Body Generated generated) {
            this.hit = true
        }
    }
}
