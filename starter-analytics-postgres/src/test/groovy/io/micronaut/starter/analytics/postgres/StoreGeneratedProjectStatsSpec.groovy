package io.micronaut.starter.analytics.postgres

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.analytics.SelectedFeature
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import jakarta.inject.Inject
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

@Property(name = "spec.name", value = "StoreGeneratedProjectStatsSpec")
@Property(name = "api-keys.test.name", value = "Mr. Tester")
@Property(name = "api-keys.test.key", value = "wonderful")
@MicronautTest(transactional = false)
@spock.lang.Requires({ DockerClientFactory.instance().isDockerAvailable() })
class StoreGeneratedProjectStatsSpec extends Specification implements TestPropertyProvider {

    @Shared @AutoCleanup PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:10")
            .withDatabaseName("test-database")
            .withUsername("test")
            .withPassword("test")

    @Override
    Map<String, String> getProperties() {
        postgres.start()

        ["datasources.default.url":postgres.getJdbcUrl(),
         "datasources.default.username":postgres.getUsername(),
         "datasources.default.password":postgres.getPassword(),
         "datasources.default.dialect": Dialect.POSTGRES.name()]
    }

    @Inject UnauthorizedAnalyticsClient unauthorizedClient
    @Inject WrongApiKeyClient wrongApiKeyClient
    @Inject AnalyticsClient client
    @Inject ApplicationRepository repository
    @Inject FeatureRepository featureRepository

    void "test save generation data without api key"() {
        given:
        def generated = new Generated(
                ApplicationType.FUNCTION,
                Language.KOTLIN,
                BuildTool.MAVEN,
                TestFramework.SPOCK,
                MicronautJdkVersionConfiguration.DEFAULT_OPTION
        )
        generated.setSelectedFeatures([new SelectedFeature("google-cloud-function")])

        when:
        unauthorizedClient.applicationGenerated(generated).get()

        then:
        ExecutionException e = thrown()
        (e.cause as HttpClientResponseException).status == HttpStatus.UNAUTHORIZED
    }

    void "test save generation data with a wrong api key"() {
        given:
        def generated = new Generated(
                ApplicationType.FUNCTION,
                Language.KOTLIN,
                BuildTool.MAVEN,
                TestFramework.SPOCK,
                MicronautJdkVersionConfiguration.DEFAULT_OPTION
        )
        generated.setSelectedFeatures([new SelectedFeature("google-cloud-function")])

        when:
        wrongApiKeyClient.applicationGenerated(generated).get()

        then:
        ExecutionException e = thrown()
        (e.cause as HttpClientResponseException).status == HttpStatus.UNAUTHORIZED
    }

    void "test save generation data"() {
        given:
        def generated = new Generated(
                ApplicationType.FUNCTION,
                Language.KOTLIN,
                BuildTool.MAVEN,
                TestFramework.SPOCK,
                MicronautJdkVersionConfiguration.DEFAULT_OPTION
        )
        generated.setSelectedFeatures([new SelectedFeature("google-cloud-function")])

        when:
        HttpStatus status = client.applicationGenerated(generated).get()

        then:
        status == HttpStatus.ACCEPTED

        when:
        Application application = repository.list(Pageable.UNPAGED).getContent()[0]

        then:
        application.type == generated.type
        application.language == generated.language
        application.buildTool == generated.buildTool
        application.jdkVersion == generated.jdkVersion
        application.testFramework == generated.testFramework
        application.features.find { it.name == 'google-cloud-function' }
        application.micronautVersion == VersionInfo.micronautVersion
        application.dateCreated

        when:
        List<TotalDTO> topFeatures = featureRepository.topFeatures()

        then:
        !topFeatures.isEmpty()
        topFeatures[0].name == 'google-cloud-function'
        topFeatures[0].total == 1

        when:
        List<TotalDTO> languages = featureRepository.topLanguages()

        then:
        languages
        Language
        languages[0].getName() == 'kotlin'
        featureRepository.topBuildTools()
        featureRepository.topJdkVersion()
        featureRepository.topTestFrameworks()
    }

    @Requires(property = "spec.name", value = "StoreGeneratedProjectStatsSpec")
    @Client(AnalyticsController.PATH)
    @Header(name = "X-API-KEY", value = "wonderful")
    static interface AnalyticsClient {
        @Post("/report")
        CompletableFuture<HttpStatus> applicationGenerated(@NonNull @Body Generated generated);
    }

    @Requires(property = "spec.name", value = "StoreGeneratedProjectStatsSpec")
    @Client(AnalyticsController.PATH)
    static interface UnauthorizedAnalyticsClient {
        @Post("/report")
        CompletableFuture<HttpStatus> applicationGenerated(@NonNull @Body Generated generated);
    }

    @Requires(property = "spec.name", value = "StoreGeneratedProjectStatsSpec")
    @Client(AnalyticsController.PATH)
    @Header(name = "X-API-KEY", value = "WRONG!")
    static interface WrongApiKeyClient {
        @Post("/report")
        CompletableFuture<HttpStatus> applicationGenerated(@NonNull @Body Generated generated);
    }
}
