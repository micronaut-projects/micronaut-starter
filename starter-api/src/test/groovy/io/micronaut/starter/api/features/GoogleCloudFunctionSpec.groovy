package io.micronaut.starter.api.features

import io.micronaut.starter.api.RequestInfo
import io.micronaut.starter.api.preview.PreviewController
import io.micronaut.starter.api.preview.PreviewDTO
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudRawFunction
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class GoogleCloudFunctionSpec extends Specification {
    @Inject PreviewController controller
    void "test google cloud function files"() {
        when:
        def map = controller.previewApp(
                ApplicationType.DEFAULT,
                "test",
                [GoogleCloudRawFunction.NAME]
        )

        then:
        map.contents.containsKey('src/main/java/test/HelloController.java')
        map.contents.containsKey('src/test/java/test/HelloFunctionTest.java')
        !map.contents.containsKey('src/main/java/test/Application.java')
    }
}
