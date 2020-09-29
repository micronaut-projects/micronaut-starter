package io.micronaut.starter.api

import edu.umd.cs.findbugs.annotations.Nullable
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.api.event.ApplicationGeneratingEvent
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.ZipUtil
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
class CreateControllerSpec extends Specification {

    @Inject
    CreateClient client

    @Inject
    MyEventListener eventListener

    void "test default create app command"() {
        when:
        def bytes = client.createApp("test", Collections.emptyList(), null, null, null)

        then:
        ZipUtil.isZip(bytes)
        eventListener.fired
    }

    void "test default create app - bad project name"() {
        when:
        client.createApp("tes%*&*t", Collections.emptyList(), null, null, null)

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.message.contains("name: must match")
    }

    void "test default create app - missing feature"() {
        when:
        client.createApp("test",['junkkkk'], null, null, null)

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.message.contains("The requested feature does not exist: junkkkk")
    }

    void "test default create app file name"() {
        when:
        def response = client.createResponse("test", Collections.emptyList(), null, null, null)
        def bytes = response.body()

        then:
        ZipUtil.isZip(bytes)
        response.header(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
    }

    void "test get zip"() {
        when:
        def response = client.getZip("test", Collections.emptyList(), null, null, null)
        def bytes = response.body()

        then:
        ZipUtil.isZip(bytes)
        response.header(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
    }

    void "test default create app with feature"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, null)

        then:
        ZipUtil.containsFileWithContents(bytes, "build.gradle", ':svm')
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

    @Singleton
    static class MyEventListener implements ApplicationEventListener<ApplicationGeneratingEvent> {
        boolean fired = false
        @Override
        void onApplicationEvent(ApplicationGeneratingEvent event) {
            fired = true
        }
    }

    @Client('/')
    static interface CreateClient {
        @Get(uri = "/create/default/{name}{?features,build,test,lang}", consumes = "application/zip")
        byte[] createApp(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang
        );

        @Get(uri = "/create/default/{name}{?features,build,test,lang}", consumes = "application/zip")
        HttpResponse<byte[]> createResponse(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang
        );

        @Get(uri = "/{name}.zip{?features,build,test,lang}", consumes = "application/zip")
        HttpResponse<byte[]> getZip(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang
        );
    }
}
