package io.micronaut.starter.cli

import io.micronaut.serde.annotation.Serdeable
import io.micronaut.starter.cli.command.BuildToolConverter
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.reflect.Type

class SerdeSpec extends Specification {

    @Unroll
    void "#type is annotated with @Serdeable"(Type type) {
        expect:
        Reflections ref = new Reflections("io.micronaut.starter.cli",
                new SubTypesScanner(),
                new TypeAnnotationsScanner())
        def classes = ref.getTypesAnnotatedWith(Serdeable)

        assert classes.contains(type)

        where:
        type << [CodeGenConfig, BuildToolConverter]
    }
}
