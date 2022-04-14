package io.micronaut.starter.api

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.api.event.ApplicationGeneratingEvent
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime
import io.micronaut.starter.feature.function.CloudProvider
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.ZipUtil
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Specification

@MicronautTest
class ZipCreateControllerSpec extends Specification {

    @Inject
    CreateClient client

    @Client("/")
    @Inject
    HttpClient httpClient

    @Inject
    MyEventListener eventListener

    void "test default create app command"() {
        when:
        def bytes = client.createApp("test", Collections.emptyList(), null, null, null, null)

        then:
        ZipUtil.isZip(bytes)
        eventListener.fired
    }

    void "test default create app - bad project name"() {
        when:
        client.createApp("tes%*&*t", Collections.emptyList(), null, null, null, null)

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.getResponse().getBody(Map).get()._embedded.errors[0].message.contains("name: must match")
    }

    void "test default create app - missing feature"() {
        when:
        client.createApp("test",['junkkkk'], null, null, null, null)

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST
        e.getResponse().getBody(Map).get()._embedded.errors[0].message.contains("The requested feature does not exist: junkkkk")
    }

    void "test default create app file name"() {
        when:
        def response = client.createResponse("test", Collections.emptyList(), null, null, null, null)
        def bytes = response.body()

        then:
        ZipUtil.isZip(bytes)
        response.header(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
    }

    void "test get zip"() {
        when:
        def response = client.getZip("test", Collections.emptyList(), null, null, null, null)
        def bytes = response.body()

        then:
        ZipUtil.isZip(bytes)
        response.header(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
    }

    void "test default create app with feature"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, null, null)

        then:
        ZipUtil.containsFileWithContents(bytes, "build.gradle", ':svm')
    }

    void "test create app with kotlin"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, Language.KOTLIN, null)

        then:
        ZipUtil.containsFile(bytes, "Application.kt")
    }

    void "test create app with groovy"() {
        when:
        def bytes = client.createApp("test", ['flyway'], null, null, Language.GROOVY, null)

        then:
        ZipUtil.containsFile(bytes, "Application.groovy")
    }

    void "test create app with maven"() {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.MAVEN, null, Language.GROOVY, null)

        then:
        ZipUtil.containsFile(bytes, "pom.xml")
    }

    void "test create app with spock"() {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.GRADLE, TestFramework.SPOCK, Language.GROOVY, null)

        then:
        ZipUtil.containsFileWithContents(bytes, "build.gradle", "spock")
    }

    void "test invalid cloud provider"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.GET('/create/default/test?cloudProvider=invalid'), Argument.of(String), Argument.of(Map))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST

        when:
        Optional<Map> response = e.response.getBody(Map)

        then:
        response.isPresent()
        response.get()._embedded.errors[0].message == 'Failed to convert argument [cloudProvider] for value [invalid] due to: No enum constant io.micronaut.starter.feature.function.CloudProvider.invalid'
    }

    void "test invalid cloud provider feature combo"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.GET("/create/default/test?cloudProvider=aws&features=$AwsLambdaCustomRuntime.NAME&features=$GoogleCloudFunction.NAME"), Argument.of(String), Argument.of(Map))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST

        when:
        Optional<Map> response = e.response.getBody(Map)

        then:
        response.isPresent()
        response.get()._embedded.errors[0].message == 'Cannot use feature [google-cloud-function-http] when Cloud Provider is set to Amazon Web Services (AWS)'
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
        @Get(uri = "/create/default/{name}{?features,build,test,lang,cloudProvider}", consumes = "application/zip")
        byte[] createApp(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang,
                @Nullable CloudProvider cloudProvider
        );

        @Get(uri = "/create/default/{name}{?features,build,test,lang,cloudProvider}", consumes = "application/zip")
        HttpResponse<byte[]> createResponse(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang,
                @Nullable CloudProvider cloudProvider
        );

        @Get(uri = "/{name}.zip{?features,build,test,lang,cloudProvider}", consumes = "application/zip")
        HttpResponse<byte[]> getZip(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang,
                @Nullable CloudProvider cloudProvider
        );
    }
}
