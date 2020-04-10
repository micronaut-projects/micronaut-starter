package io.micronaut.starter.feature.lang

import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.Swagger
import io.micronaut.starter.feature.lang.java.application
import io.micronaut.starter.fixture.ProjectFixture
import spock.lang.Specification

class JavaApplicationSpec extends Specification implements ProjectFixture {

    void "test java application"() {
        String applicationJava = application.template(buildProject(), new Features([]))
        .render()
        .toString()

        expect:
        applicationJava.contains("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
""".trim())
    }

    void "test java application with swagger"() {
        String applicationJava = application.template(buildProject(), new Features([new Swagger()]))
                .render()
                .toString()

        expect:
        applicationJava.contains("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "foo",
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
