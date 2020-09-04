package io.micronaut.starter.feature.lang

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.feature.lang.java.application
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class JavaApplicationSpec extends Specification implements ProjectFixture, ContextFixture, CommandOutputFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    void 'Application file is generated for a default application type with gradle and referenced in build.gradle mainClassName for language: java'() {
        given:
        def policy = VersionInfo.isMicronautSnapshot() ? "enforcedPlatform" : "platform"
        def testPolicy = "enforcedPlatform"
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE),
                []
        )

        then:
        output.containsKey("src/main/java/example/micronaut/Application.${Language.JAVA.extension}".toString())

        when:
        def buildGradle = output['build.gradle']

        then:
        buildGradle.contains('mainClassName = "example.micronaut.Application"')
    }

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
        Micronaut.run(Application.class, args);
    }
}
""".trim())
    }

    void "test java application with openapi"() {
        String applicationJava = application.template(buildProject(), getFeatures(["openapi"]))
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
        Micronaut.run(Application.class, args);
    }
}
""".trim())
    }
}
