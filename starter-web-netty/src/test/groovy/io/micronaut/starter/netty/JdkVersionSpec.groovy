package io.micronaut.starter.netty

import io.micronaut.json.JsonMapper
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class JdkVersionSpec extends Specification {
    @Inject
    JsonMapper jsonMapper

    void "Serialize and deserialize JDKVersion"(JdkVersion jdkVersion) {
        given:
        int jdk = jdkVersion.majorVersion()

        when:
        String json = jsonMapper.writeValueAsString(new Foo(jdk: jdkVersion))

        then:
        json.contains("{\"jdk\":\"JDK_${jdk}\"}")

        when:
        Foo foo = jsonMapper.readValue("{\"jdk\":\"JDK_${jdk}\"}", Foo.class)

        then:
        jdkVersion == foo.jdk

        where:
        jdkVersion << MicronautJdkVersionConfiguration.SUPPORTED_JDKS
    }

    @Serdeable
    static class Foo {
        JdkVersion jdk
    }
}
