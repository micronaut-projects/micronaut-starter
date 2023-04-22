package io.micronaut.starter.api.features

import io.micronaut.starter.api.RequestInfo
import io.micronaut.starter.api.preview.PreviewController
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.gcp.GoogleCloudEventsFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudRawFunction
import io.micronaut.starter.options.JdkVersion
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Requires
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudFunctionSpec extends Specification {

    @Inject PreviewController controller

    void "test google cloud function files"() {
        when:
        def map = controller.previewApp(
                ApplicationType.DEFAULT,
                "test",
                [GoogleCloudRawFunction.NAME],
                null,
                null,
                null,
                JdkVersion.JDK_11,
                new RequestInfo("http://localhost", "", null, Locale.ENGLISH, "")
        )

        then:
        map.contents.containsKey('src/main/java/test/TestController.java')
        map.contents.containsKey('src/test/java/test/TestFunctionTest.java')
        map.contents.containsKey('src/main/java/test/Application.java')
    }

    void "test google cloudevents function files"() {
        when:
        def map = controller.previewApp(
                ApplicationType.FUNCTION,
                "test",
                [GoogleCloudEventsFunction.NAME],
                null,
                null,
                null,
                JdkVersion.JDK_11,
                new RequestInfo("http://localhost", "", null, Locale.ENGLISH, "")
        )

        then:
        map.contents.containsKey('src/main/java/test/Function.java')
        map.contents.containsKey('src/test/java/test/FunctionTest.java')
        map.contents['src/main/java/test/Function.java'].contains('StorageObjectData')
        map.contents['build.gradle'].contains('io.micronaut.gcp:micronaut-gcp-function-cloudevents')
    }
}
