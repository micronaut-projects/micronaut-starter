package io.micronaut.starter.api

import io.micronaut.json.JsonMapper
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class JdkVersionDTOSpec extends Specification {
    @Inject
    JsonMapper jsonMapper

    void "Serialize JdkVersionDTO with jdk = #jdkVersion"(JdkVersion jdkVersion) {
        when:
        String json = jsonMapper.writeValueAsString(new JdkVersionDTO(jdkVersion))
        int jdk = jdkVersion.majorVersion()
        then:
        json.contains("\"value\":\"JDK_${jdk}\"")
        json.contains("\"label\":\"${jdk}\"}")
        json.contains("\"name\":\"JDK_${jdk}\"")

        where:
        jdkVersion << MicronautJdkVersionConfiguration.SUPPORTED_JDKS
    }
}
