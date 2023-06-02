package io.micronaut.starter.feature.testresources

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.feature.database.DataHibernateReactive
import io.micronaut.starter.feature.database.DataJdbc
import io.micronaut.starter.feature.database.DataJpa
import io.micronaut.starter.feature.database.DataMongo
import io.micronaut.starter.feature.database.HibernateReactiveJpa
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MongoReactive
import io.micronaut.starter.feature.database.MongoSync
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.feature.messaging.mqtt.Mqtt
import io.micronaut.starter.feature.messaging.rabbitmq.RabbitMQ
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class OptionalTestContainerSpec extends ApplicationContextSpec {

    private final static TEST_RESOURCE_FEATURES = [
            MySQL.NAME,
            PostgreSQL.NAME,
            Oracle.NAME,
            MariaDB.NAME,
            SQLServer.NAME,
            DataMongo.NAME,
            DataJdbc.NAME,
            DataJpa.NAME,
            RabbitMQ.NAME,
            Mqtt.NAME,
            MongoSync.NAME,
            MongoReactive.NAME,
            HibernateReactiveJpa.NAME,
            DataHibernateReactive.NAME,
    ]

    static List<String> extraModules(String name) {
        name in [DataJpa.NAME, DataJdbc.NAME, HibernateReactiveJpa.NAME, DataHibernateReactive.NAME] ? [MySQL.NAME] : []
    }

    void "test resources for #features is enabled by default"() {
        given:
        def ctx = buildGeneratorContext(features)

        expect:
        ctx.isFeaturePresent(TestResources)
        !ctx.isFeaturePresent(TestContainers)

        where:
        name << TEST_RESOURCE_FEATURES
        features = [name] + extraModules(name)
    }

    void "adding features #features removes TestResources"() {
        given:
        def ctx = buildGeneratorContext(features)

        expect:
        !ctx.isFeaturePresent(TestResources)
        ctx.isFeaturePresent(TestContainers)

        where:
        name << TEST_RESOURCE_FEATURES
        features = [name] + extraModules(name) + [TestContainers.NAME]
    }
}
