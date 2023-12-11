package io.micronaut.starter.api

import io.micronaut.json.JsonMapper
import io.micronaut.starter.options.JdkVersion
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class JdkVersionDTOTest extends Specification {
    @Inject
    JsonMapper jsonMapper

    void "Serialize JDKVersionDTO"() {
        when:
        String json = jsonMapper.writeValueAsString(new JdkVersionDTO(JdkVersion.JDK_17))
        then:
        json.contains('"value":"JDK_17"')
        json.contains('"label":"17"}')
        json.contains('"name":"JDK_17"')
    }
}
