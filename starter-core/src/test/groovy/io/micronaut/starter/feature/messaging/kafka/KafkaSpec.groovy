package io.micronaut.starter.feature.messaging.kafka

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

class KafkaSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature kafka contains links to micronaut docs'() {
        when:
        def output = generate(['kafka'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["kafka"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["kafka"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kafka</groupId>
      <artifactId>micronaut-kafka</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['kafka'])

        then:
        ctx.configuration.containsKey('kafka.bootstrap.servers')
    }
}
