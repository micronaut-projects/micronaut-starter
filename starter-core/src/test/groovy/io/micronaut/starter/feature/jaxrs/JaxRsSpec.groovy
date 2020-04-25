package io.micronaut.starter.feature.jaxrs

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JaxRsSpec extends BeanContextSpec {

    @Unroll
    void 'test jax-rs with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['jax-rs'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.jaxrs:micronaut-jaxrs-server")')
        template.contains("$scope(\"io.micronaut.jaxrs:micronaut-jaxrs-processor\")")

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven jax-rs feature'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['jax-rs'], Language.JAVA), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-server</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.jaxrs</groupId>
              <artifactId>micronaut-jaxrs-processor</artifactId>
              <version>\${micronaut.jaxrs.version}</version>
            </path>
""")
        template.contains("""
                <path>
                  <groupId>io.micronaut.jaxrs</groupId>
                  <artifactId>micronaut-jaxrs-processor</artifactId>
                  <version>\${micronaut.jaxrs.version}</version>
                </path>
""")

        when:
        template = pom.template(buildProject(), getFeatures(['jax-rs'], Language.KOTLIN), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-server</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.count("""
                <annotationProcessorPath>
                    <groupId>io.micronaut.jaxrs</groupId>
                    <artifactId>micronaut-jaxrs-processor</artifactId>
                    <version>\${micronaut.jaxrs.version}</version>
                </annotationProcessorPath>
""") == 2

        when:
        template = pom.template(buildProject(), getFeatures(['jax-rs'], Language.GROOVY), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-server</artifactId>
      <scope>compile</scope>
    </dependency>
""")

    }
}
