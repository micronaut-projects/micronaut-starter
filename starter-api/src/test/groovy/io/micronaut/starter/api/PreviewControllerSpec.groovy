package io.micronaut.starter.api

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.api.preview.PreviewDTO
import io.micronaut.starter.api.preview.PreviewOperation
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.annotation.Nullable
import javax.inject.Inject

@MicronautTest
class PreviewControllerSpec extends Specification {
    @Inject
    PreviewClient client

    void "test default create app command"() {
        when:
        def map = client.previewApp(ApplicationTypes.app, "test", Collections.emptyList(), null, null, null)

        then:
        map.contents.containsKey("build.gradle")

    }

    @Client('/preview')
    static interface PreviewClient  {
        @Get(uri = "/app/{name}{?features,build,test,lang}", consumes = MediaType.APPLICATION_JSON)
        PreviewDTO previewApp(
                ApplicationTypes type,
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang
        );
    }
}
