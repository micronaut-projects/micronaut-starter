package io.micronaut.starter.analytics.postgres

import io.micronaut.core.annotation.NonNull;
import io.micronaut.context.env.Environment
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.runtime.config.SchemaGenerate
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.analytics.Generated
import io.micronaut.starter.analytics.SelectedFeature
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject
import java.util.concurrent.CompletableFuture

@MicronautTest(
        transactional = false,
        environments = Environment.GOOGLE_COMPUTE)
class StoreGeneratedProjectStatsSpec extends Specification implements TestPropertyProvider {
    @Shared @AutoCleanup PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:10")
            .withDatabaseName("test-database")
            .withUsername("test")
            .withPassword("test")


    @Override
    Map<String, String> getProperties() {
        postgres.start()

        return [
                "datasources.default.url":postgres.getJdbcUrl(),
                "datasources.default.username":postgres.getUsername(),
                "datasources.default.password":postgres.getPassword(),
                "datasources.default.schema-generate": SchemaGenerate.CREATE.name(),
                "datasources.default.dialect": Dialect.POSTGRES.name()
        ]
    }

    @Inject AnalyticsClient client
    @Inject ApplicationRepository repository
    @Inject FeatureRepository featureRepository

    void "test save generation data"() {
        given:
        def generated = new Generated(
                ApplicationType.FUNCTION,
                Language.KOTLIN,
                BuildTool.MAVEN,
                TestFramework.SPOCK,
                JdkVersion.JDK_11
        )
        generated.setSelectedFeatures([new SelectedFeature("google-cloud-function")])

        when:
        HttpStatus status = client.applicationGenerated(
                generated
        ).get()


        then:
        status == HttpStatus.ACCEPTED

        when:
        def application = repository.list(Pageable.UNPAGED)[0]

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
        def topFeatures = featureRepository.topFeatures()

        then:
        !topFeatures.isEmpty()
        topFeatures[0].name == 'google-cloud-function'
        topFeatures[0].total == 1

        when:
        def languages = featureRepository.topLanguages()

        then:
        languages
        languages[0].name == 'kotlin'
        featureRepository.topBuildTools()
        featureRepository.topJdkVersion()
        featureRepository.topTestFrameworks()
    }

    @Client("/analytics")
    static interface AnalyticsClient {
        @Post("/report")
        CompletableFuture<HttpStatus> applicationGenerated(@NonNull @Body Generated generated);
    }
}
