package io.micronaut.starter.feature.lang

import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.Swagger
import io.micronaut.starter.util.NameUtils
import spock.lang.Specification
import io.micronaut.starter.feature.lang.java.application

class JavaApplicationSpec extends Specification {

    void "test java application"() {
        String applicationJava = application.template(NameUtils.parse("abc.def"), new Features([]))
        .render()
        .toString()

        expect:
        applicationJava.contains("""
package abc;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
""".trim())
    }

    void "test java application with swagger"() {
        String applicationJava = application.template(NameUtils.parse("abc.def"), new Features([new Swagger()]))
                .render()
                .toString()

        expect:
        applicationJava.contains("""
package abc;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "def",
            version = "0.0"
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
""".trim())
    }
}
