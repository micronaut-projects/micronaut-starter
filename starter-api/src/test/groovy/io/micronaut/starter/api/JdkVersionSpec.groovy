package io.micronaut.starter.api

import io.micronaut.json.JsonMapper
import io.micronaut.starter.options.JdkVersion
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class JdkVersionSpec extends Specification {
    @Inject
    JsonMapper jsonMapper

    void "Serialize JDKVersion"() {
        given:
        JdkVersion jdk17 = JdkVersion.JDK_17

        when:
        String json = jsonMapper.writeValueAsString(jdk17)

        then:
        json.contains("JDK_17")

        when:
        JdkVersion jdkVersion = jsonMapper.readValue("JDK_17", JdkVersion.class)

        then:
        jdk17 == jdkVersion
    }
}
