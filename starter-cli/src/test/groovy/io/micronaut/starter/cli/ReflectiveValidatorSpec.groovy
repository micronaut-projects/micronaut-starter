package io.micronaut.starter.cli

import io.micronaut.core.annotation.ReflectiveAccess
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.spockframework.util.Assert
import picocli.CommandLine
import spock.lang.Specification

import java.lang.reflect.Field

class ReflectiveValidatorSpec extends Specification{

    void "validate all CommandLine Option are setup to be used with graalvm"(){
        expect:
        Reflections ref = new Reflections("io.micronaut.starter.cli",
                new FieldAnnotationsScanner())

        def fields = ref.getFieldsAnnotatedWith(CommandLine.Option)

        for(Field field : fields){
            def annotation = field.getAnnotation(ReflectiveAccess)
            if(!annotation) {
                Assert.fail("For options to work in graalvm you must add @ReflectiveAccess to all @CommandLine.Option fields. Class : %s, Field : %s", field.declaringClass.getSimpleName(), field.name)
            }
        }
    }
}
