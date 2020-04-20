package io.micronaut.starter.feature.lang

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.feature.lang.java.application
import io.micronaut.starter.fixture.ProjectFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class JavaApplicationSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    void "test java application"() {
        String applicationJava = application.template(buildProject(), getFeatures([]))
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
        String applicationJava = application.template(buildProject(), getFeatures(["swagger"]))
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
