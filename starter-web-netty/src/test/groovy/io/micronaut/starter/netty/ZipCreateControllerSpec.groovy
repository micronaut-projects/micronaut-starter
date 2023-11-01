package io.micronaut.starter.netty

import io.micronaut.core.annotation.Nullable
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.ZipUtil
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Issue
import spock.lang.Specification

@MicronautTest
class ZipCreateControllerSpec extends Specification {

    @Inject
    CreateClient client

    @Client("/")
    @Inject
    HttpClient httpClient

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/352")
    void "micronaut is not an allowed name"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.GET('/create/default/micronaut'), Argument.of(String), Argument.of(Map))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST

        when:
        Optional<Map> response = e.response.getBody(Map)

        then:
        response.isPresent()
        response.get()._embedded.errors[0].message == 'Invalid project name: \"micronaut\" is not a valid app name'

        when:
        httpClient.toBlocking().exchange(HttpRequest.GET('/create/default/com.example.micronaut'), Argument.of(String), Argument.of(Map))

        then:
        e = thrown()
        e.status == HttpStatus.BAD_REQUEST

        when:
        response = e.response.getBody(Map)

        then:
        response.isPresent()
        response.get()._embedded.errors[0].message == 'Invalid project name: \"micronaut\" is not a valid app name'

        when:
        httpClient.toBlocking().exchange(HttpRequest.GET('/micronaut.zip'), Argument.of(String), Argument.of(Map))

        then:
        e = thrown()
        e.status == HttpStatus.BAD_REQUEST

        when:
        response = e.response.getBody(Map)

        then:
        response.isPresent()
        response.get()._embedded.errors[0].message == 'Invalid project name: \"micronaut\" is not a valid app name'
    }

    void "test default create app command"() {
        when:
        def bytes = client.createApp("test", Collections.emptyList(), null, null, null)

        then:
        ZipUtil.isZip(bytes)

    }

    void "test default create app with feature"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, null)

        then:
        ZipUtil.containsFile(bytes, "build.gradle.kts")
        !ZipUtil.containsFileWithContents(bytes, "build.gradle.kts", ':svm')
    }

    void "test create app with kotlin"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, Language.KOTLIN)

        then:
        ZipUtil.containsFile(bytes, "Application.kt")
    }

    void "test create app with groovy"() {
        when:
        def bytes = client.createApp("test", ['flyway'], null, null, Language.GROOVY)

        then:
        ZipUtil.containsFile(bytes, "Application.groovy")
    }

    void "test create app with maven"() {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.MAVEN, null, Language.GROOVY)

        then:
        ZipUtil.containsFile(bytes, "pom.xml")
    }

    void "test create app with spock"() {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.GRADLE, TestFramework.SPOCK, Language.GROOVY)

        then:
        ZipUtil.containsFileWithContents(bytes, "build.gradle", "spock")
    }

    @Client('/create')
    static interface CreateClient {
        @Get(uri = "/default/{name}{?features,build,test,lang}", consumes = "application/zip")
        byte[] createApp(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang
        );
    }
}
