package io.micronaut.starter.generator

import io.micronaut.starter.feature.messaging.kafka.Kafka
import io.micronaut.starter.feature.messaging.rabbitmq.RabbitMQ
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateMessagingSpec extends CommandSpec {

    @Unroll
    void 'test basic create-messaging-app for #feature and #language and #buildTool'(Language language, BuildTool buildTool, String feature) {
        given:
        generateMessagingProject(language, buildTool, [feature])

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        [language, buildTool, feature] << [Language.values(), BuildTool.values(), [Kafka.NAME, RabbitMQ.NAME]].combinations()
    }

}
