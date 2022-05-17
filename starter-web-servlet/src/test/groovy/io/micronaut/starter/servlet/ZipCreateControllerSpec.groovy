package io.micronaut.starter.servlet

import io.micronaut.core.annotation.Nullable
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime
import io.micronaut.starter.feature.function.CloudProvider
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.ZipUtil
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Retry
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
@Retry
class ZipCreateControllerSpec extends Specification {

    @Client("/")
    @Inject
    HttpClient httpClient

    @Inject
    CreateClient client

    void "test default create app command"() {
        when:
        def bytes = client.createApp("test", Collections.emptyList(), null, null, null, null)

        then:
        ZipUtil.isZip(bytes)
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

    @Client('/create')
    static interface CreateClient {
        @Get(uri = "/default/{name}{?features,build,test,lang,cloudProvider}", consumes = "application/zip")
        byte[] createApp(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang,
                @Nullable CloudProvider cloudProvider
        );
    }
}
