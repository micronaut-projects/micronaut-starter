package io.micronaut.starter.analytics.postgres

import edu.umd.cs.findbugs.annotations.NonNull
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
import io.micronaut.test.annotation.MicronautTest
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

    void "test save generation data"() {
        given:
        def generated = new Generated(
                ApplicationType.FUNCTION,
                Language.KOTLIN,
                BuildTool.MAVEN,
                TestFramework.SPOCK,
                JdkVersion.JDK_9
        )
        generated.setFeatures([new SelectedFeature("azure-function")])

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
        application.features.find { it.name == 'azure-function' }
    }

    @Client("/analytics")
    static interface AnalyticsClient {
        @Post("/report")
        CompletableFuture<HttpStatus> applicationGenerated(@NonNull @Body Generated generated);
    }
}
