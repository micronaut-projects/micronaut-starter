package io.micronaut.starter.feature.lang

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.lang.java.application
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo

class JavaApplicationSpec extends BeanContextSpec implements CommandOutputFixture {

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
        buildGradle.contains('mainClass.set("example.micronaut.Application")')
    }

    void "test java application"() {
        String applicationJava = application.template(buildProject(), getFeatures([]), null, false)
        .render()
        .toString()

        expect:
        applicationJava.contains("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
            .mainClass(Application.class)
            .start();
    }
}
""".trim())
    }

    void "test java application with eagerInit"() {
        String applicationJava = application.template(buildProject(), getFeatures([]), null, true)
                .render()
                .toString()

        expect:
        applicationJava.contains("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
            .mainClass(Application.class)
            .eagerInitSingletons(true)
            .start();
    }
}
""".trim())
    }

    void "test java application with openapi"() {
        String applicationJava = application.template(buildProject(), getFeatures(["openapi"]), null, false)
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
        Micronaut.build(args)
            .mainClass(Application.class)
            .start();
    }
}
""".trim())
    }

    void "test java application with dekorate-kubernetes"() {
        String applicationJava = application.template(buildProject(), getFeatures(["dekorate-kubernetes"]), null, false)
                .render()
                .toString()

        expect:
        applicationJava.contains("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;
import io.dekorate.kubernetes.annotation.KubernetesApplication;
import io.dekorate.kubernetes.annotation.Label;
import io.dekorate.kubernetes.annotation.Port;
import io.dekorate.kubernetes.annotation.Probe;

@KubernetesApplication(
    name = "foo",
    labels = @Label(key = "app", value = "foo"),
    ports = @Port(name = "http", containerPort = 8080),
    livenessProbe = @Probe(httpActionPath = "/health/liveness", initialDelaySeconds = 5, timeoutSeconds = 3, failureThreshold = 10),
    readinessProbe = @Probe(httpActionPath = "/health/readiness", initialDelaySeconds = 5, timeoutSeconds = 3, failureThreshold = 10)
)
public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
            .mainClass(Application.class)
            .start();
    }
}
""".trim())
    }
}
