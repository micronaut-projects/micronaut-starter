package io.micronaut.starter.analytics.postgres

import io.micronaut.context.env.Environment
import io.micronaut.core.annotation.NonNull
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
import io.micronaut.starter.feature.function.CloudProvider
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

import jakarta.inject.Inject
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

        ["datasources.default.url":postgres.getJdbcUrl(),
         "datasources.default.username":postgres.getUsername(),
         "datasources.default.password":postgres.getPassword(),
         "datasources.default.schema-generate": SchemaGenerate.CREATE.name(),
         "datasources.default.dialect": Dialect.POSTGRES.name()]
    }

    @Inject AnalyticsClient client
    @Inject ApplicationRepository repository
    @Inject FeatureRepository featureRepository

    void "test save generation data"() {
        given:
        def generated = [
                new Generated(ApplicationType.FUNCTION, Language.KOTLIN, BuildTool.MAVEN, TestFramework.KOTEST, JdkVersion.JDK_11, CloudProvider.GCP).with {
                    it.setSelectedFeatures([new SelectedFeature("google-cloud-function")])
                    it
                },
                new Generated(ApplicationType.DEFAULT, Language.GROOVY, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_11, null).with {
                    it.setSelectedFeatures([new SelectedFeature("graalvm")])
                    it
                },
                new Generated(ApplicationType.DEFAULT, Language.JAVA, BuildTool.GRADLE, TestFramework.SPOCK, JdkVersion.JDK_11, null).with {
                    it.setSelectedFeatures([new SelectedFeature("graalvm")])
                    it
                }
        ]

        when:
        def status = generated.collect {
            client.applicationGenerated(it).get()
        }

        then:
        status.every { it == HttpStatus.ACCEPTED }

        when:
        def applications = repository.list(Pageable.UNPAGED)

        then:
        with(applications[0]) {
            type == generated[0].type
            language == generated[0].language
            buildTool == generated[0].buildTool
            jdkVersion == generated[0].jdkVersion
            testFramework == generated[0].testFramework
            cloudProvider == generated[0].cloudProvider
            features.find { it.name == 'google-cloud-function' }
            micronautVersion == VersionInfo.micronautVersion
            dateCreated
        }

        and:
        with(applications[1]) {
            type == generated[1].type
            language == generated[1].language
            buildTool == generated[1].buildTool
            jdkVersion == generated[1].jdkVersion
            testFramework == generated[1].testFramework
            !cloudProvider
            features.find { it.name == 'graalvm' }
            micronautVersion == VersionInfo.micronautVersion
            dateCreated
        }

        and:
        with(applications[2]) {
            type == generated[2].type
            language == generated[2].language
            buildTool == generated[2].buildTool
            jdkVersion == generated[2].jdkVersion
            testFramework == generated[2].testFramework
            !cloudProvider
            features.find { it.name == 'graalvm' }
            micronautVersion == VersionInfo.micronautVersion
            dateCreated
        }

        when:
        def topFeatures = featureRepository.topFeatures()

        then:
        topFeatures.name == ['google-cloud-function', 'graalvm']
        topFeatures.total == [1, 2]

        when:
        def topCloudProviders = featureRepository.topCloudProviders()

        then:
        topCloudProviders.name == [CloudProvider.GCP.name(), null]
        topCloudProviders.total == [1, 2]

        when:
        def languages = featureRepository.topLanguages()

        then:
        languages.name == ['groovy', 'java', 'kotlin']
        languages.total == [1, 1, 1]

        when:
        def buildTools = featureRepository.topBuildTools()

        then:
        buildTools.name == ['maven', 'gradle']
        buildTools.total == [1, 2]

        when:
        def jdkVersions = featureRepository.topJdkVersion()

        then:
        jdkVersions.name == [JdkVersion.JDK_11.name()]
        jdkVersions.total == [3]

        when:
        def testFrameworks = featureRepository.topTestFrameworks()

        then:
        testFrameworks.name == ['kotest', 'spock']
        testFrameworks.total == [1, 2]
    }

    @Client("/analytics")
    static interface AnalyticsClient {
        @Post("/report")
        CompletableFuture<HttpStatus> applicationGenerated(@NonNull @Body Generated generated);
    }
}
