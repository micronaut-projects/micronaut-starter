package io.micronaut.starter.api

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.api.preview.PreviewDTO
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class PreviewControllerSpec extends Specification {
    @Inject
    PreviewClient client

    void "test default create app command"() {
        when:
        def map = client.previewApp(ApplicationType.DEFAULT, "test", Collections.emptyList(), null, null, null)

        then:
        map.contents.containsKey("build.gradle.kts")

    }

    void "test preview - bad feature"() {
        when:
        def map = client.previewApp(ApplicationType.DEFAULT, "test", ['juikkkk'], null, null, null)

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.getResponse().getBody(Map).get()._embedded.errors[0].message == 'The requested feature does not exist: juikkkk'
    }

    @Client('/preview')
    static interface PreviewClient  {
        @Get(uri = "/default/{name}{?features,build,test,lang}", consumes = MediaType.APPLICATION_JSON)
        PreviewDTO previewApp(
                ApplicationType type,
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang
        );
    }
}
