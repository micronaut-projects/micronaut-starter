package io.micronaut.starter.feature.jaxrs

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JaxRsSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jax-rs contains links to micronaut docs'() {
        when:
        def output = generate(['jax-rs'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html")
    }


    @Unroll
    void 'test jax-rs with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jax-rs'], language), false).render().toString()

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
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jax-rs'], Language.JAVA), []).render().toString()

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
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jax-rs'], Language.KOTLIN), []).render().toString()

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
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jax-rs'], Language.GROOVY), []).render().toString()

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
