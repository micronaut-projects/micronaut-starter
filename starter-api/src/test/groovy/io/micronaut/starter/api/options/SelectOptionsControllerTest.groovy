package io.micronaut.starter.api.options

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.api.JdkVersionDTO
import io.micronaut.starter.api.SelectOptionsDTO
import io.micronaut.starter.options.BuildTool
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class SelectOptionsControllerTest extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient

    void "Only JDK 17 and 21 are supported"() {
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/select-options")

        when:
        SelectOptionsDTO selectOptionsDTO = client.retrieve(request, SelectOptionsDTO)

        then:
        noExceptionThrown()
        ['JDK_17', 'JDK_21'] == selectOptionsDTO.jdkVersion.options.name

        and:
        selectOptionsDTO.jdkVersion.defaultOption.name == 'JDK_21'
    }

    void "default build tool is Gradle Kotlin"() {
        BlockingHttpClient client = httpClient.toBlocking()

        HttpRequest<?> request = HttpRequest.GET("/select-options")
        when:
        SelectOptionsDTO selectOptionsDTO = client.retrieve(request, SelectOptionsDTO)

        then:
        noExceptionThrown()

        and: "order is as expected"
        selectOptionsDTO.build.options.value == [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN]

        and: "the default is Gradle Kotlin"
        selectOptionsDTO.build.defaultOption.value == BuildTool.GRADLE_KOTLIN
    }
}
