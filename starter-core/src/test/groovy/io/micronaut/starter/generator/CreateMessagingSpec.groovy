package io.micronaut.starter.generator

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.messaging.kafka.Kafka
import io.micronaut.starter.feature.messaging.rabbitmq.RabbitMQ
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Unroll

class CreateMessagingSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test basic create-messaging-app for lang=#lang and feature=#feature'() {
        given:
        generateMessagingProject(lang, BuildTool.GRADLE, [feature])

        when:
        executeGradleCommand('run')

        then:
        testOutputContains("Startup completed")

        where:
        [lang, feature] << [[Language.JAVA, Language.GROOVY, Language.KOTLIN, null], [Kafka.NAME, RabbitMQ.NAME]].combinations()
    }

    @Unroll
    void 'test basic create-messaging-app for lang=#lang and feature=#feature and maven'() {
        given:
        generateMessagingProject(lang, BuildTool.MAVEN, [feature])

        when:
        executeMavenCommand("mn:run")

        then:
        testOutputContains("Startup completed")

        where:
        [lang, feature] << [[Language.JAVA, Language.GROOVY, Language.KOTLIN, null], [Kafka.NAME, RabbitMQ.NAME]].combinations()
    }

}
