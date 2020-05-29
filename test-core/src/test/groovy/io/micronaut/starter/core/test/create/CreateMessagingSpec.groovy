package io.micronaut.starter.core.test.create

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.messaging.kafka.Kafka
import io.micronaut.starter.feature.messaging.nats.Nats
import io.micronaut.starter.feature.messaging.rabbitmq.RabbitMQ
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Unroll

class CreateMessagingSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-messaging-createmessagingspec"
    }

    @Unroll
    void 'test basic create-messaging-app for #feature and #lang and #buildTool'(Language lang,
                                                                                 BuildTool buildTool,
                                                                                 String feature) {
        given:
        ApplicationType applicationType = ApplicationType.MESSAGING
        generateProject(lang, buildTool, [feature], applicationType)

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        [lang, buildTool, feature] << LanguageBuildCombinations.combinations([Kafka.NAME, RabbitMQ.NAME, Nats.NAME])
    }
}
