package io.micronaut.starter.feature.messaging.mqtt

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
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
        feature.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "mqtt-hivemq overrides Feature->getFrameworkDocumentation"() {
        expect:
        feature.getFrameworkDocumentation(null)
    }

    void "mqtt-hivemq overrides Feature->getThirdPartyDocumentation"() {
        expect:
        feature.getThirdPartyDocumentation(null)
    }

    void "AOP belongs to API category"() {
        expect:
        Category.MESSAGING == feature.category
    }
}
