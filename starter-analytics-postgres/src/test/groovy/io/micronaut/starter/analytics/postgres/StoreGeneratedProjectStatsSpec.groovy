package io.micronaut.starter.analytics.postgres

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.runtime.config.SchemaGenerate
import io.micronaut.starter.api.event.ApplicationGeneratingEvent
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.NameUtils
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.inject.Inject

@MicronautTest(transactional = false)
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

    @Inject ApplicationEventPublisher eventPublisher
    @Inject ApplicationRepository repository

    void "test save generation data"() {
        when:
        eventPublisher.publishEvent(new ApplicationGeneratingEvent(
                new GeneratorContext(
                        NameUtils.parse("test"),
                        ApplicationType.FUNCTION,
                        new Options(Language.KOTLIN, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_9),
                        [new io.micronaut.starter.feature.Feature() {
                            @Override
                            String getName() {
                                "test-feature"
                            }

                            @Override
                            boolean supports(ApplicationType applicationType) {
                                true
                            }
                        }] as Set
                )
        ))

        PollingConditions conditions = new PollingConditions()

        then:
        conditions.eventually {
            def list = repository.list(Pageable.UNPAGED)
            list.size() == 1
            list[0].buildTool == BuildTool.MAVEN
            list[0].type == ApplicationType.FUNCTION
            list[0].language == Language.KOTLIN
            list[0].testFramework == TestFramework.SPOCK
            list[0].jdkVersion == JdkVersion.JDK_9
            list[0].features.first().name == 'test-feature'
        }

    }

}
