package io.micronaut.starter.api

import edu.umd.cs.findbugs.annotations.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.annotation.Nullable
import javax.inject.Inject
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@MicronautTest
class DiffControllerSpec extends Specification {
    @Inject
    DiffClient diffClient

    void 'test diff'() {
        when:
        def result = diffClient.diffFeature(
                ApplicationType.DEFAULT,
                null,
                "azure-function",
                null,
                null,
                null,
                null
        )

        then:
        result.contains('+# Micronaut and Azure Function')
    }

    void 'test diff invalid feature'() {
        when:
        diffClient.diffFeature(
                ApplicationType.DEFAULT,
                null,
                "junkkkkk",
                null,
                null,
                null,
                null
        )

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.message == 'The requested feature does not exist: junkkkkk'
    }

    @Client('/diff')
    static interface DiffClient {
        @Get(uri = "/{type}/feature/{feature}{?lang,build,test,javaVersion,name}",
                consumes = MediaType.TEXT_PLAIN)
        String diffFeature(
                @NotNull ApplicationType type,
                @Nullable String name,
                @NotBlank @NonNull String feature,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang,
                @Nullable JdkVersion javaVersion);
    }
}
