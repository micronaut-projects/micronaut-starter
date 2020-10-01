package io.micronaut.starter.feature.messaging.rabbitmq

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

class RabbitMQSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature rabbitmq contains links to micronaut docs'() {
        when:
        def output = generate(['rabbitmq'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/index.html")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["rabbitmq"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.rabbitmq:micronaut-rabbitmq")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["rabbitmq"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rabbitmq</groupId>
      <artifactId>micronaut-rabbitmq</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['rabbitmq'])

        then:
        ctx.configuration.containsKey('rabbitmq.uri')
    }
}
