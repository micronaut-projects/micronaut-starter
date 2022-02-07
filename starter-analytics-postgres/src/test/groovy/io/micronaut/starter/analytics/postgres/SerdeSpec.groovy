package io.micronaut.starter.analytics.postgres

import io.micronaut.serde.annotation.Serdeable
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import spock.lang.Specification

import java.lang.reflect.Type

class SerdeSpec extends Specification {

    void "#type is annotated with @Serdeable.Deserializable"(Type type) {
        expect:
        Reflections ref = new Reflections("io.micronaut.starter",
                new SubTypesScanner(),
                new TypeAnnotationsScanner())
        def classes = ref.getTypesAnnotatedWith(Serdeable)

        assert classes.contains(type)

        where:
        type << [TotalDTO]
    }
}
