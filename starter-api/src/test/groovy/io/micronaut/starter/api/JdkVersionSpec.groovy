package io.micronaut.starter.api

import io.micronaut.core.annotation.Introspected
import io.micronaut.json.JsonMapper
import io.micronaut.starter.options.JdkVersion
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class JdkVersionSpec extends Specification {
    @Inject
    JsonMapper jsonMapper

    void "Serialize and deserialize JDKVersion"() {
        given:
        JdkVersion jdk17 = JdkVersion.JDK_17

        when:
        String json = jsonMapper.writeValueAsString(new Foo(jdk: jdk17))

        then:
        json.contains("{\"jdk\":\"JDK_17\"}")

        when:
        Foo foo = jsonMapper.readValue("{\"jdk\":\"JDK_17\"}", Foo.class)

        then:
        jdk17 == foo.jdk
    }

    @Introspected
    static class Foo {
        JdkVersion jdk
    }
}
