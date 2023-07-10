package io.micronaut.starter.feature.messaging.mqtt

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Shared
import spock.lang.Subject

class MqttHiveMqSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    MqttHiveMq feature = beanContext.getBean(MqttHiveMq)

    void "mqtt-hivemq supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "mqtt-hivemq overrides Feature->getMicronautDocumentation"() {
        expect:
        feature.micronautDocumentation
    }

    void "mqtt-hivemq overrides Feature->getThirdPartyDocumentation"() {
        expect:
        feature.thirdPartyDocumentation
    }

    void "AOP belongs to API category"() {
        expect:
        Category.MESSAGING == feature.category
    }
}
