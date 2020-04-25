package io.micronaut.starter.feature.messaging.rabbitmq

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class RabbitMQSpec extends BeanContextSpec {

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["rabbitmq"])).render().toString()

        then:
        template.contains('implementation("io.micronaut.configuration:micronaut-rabbitmq")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(buildProject(), getFeatures(["rabbitmq"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
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
