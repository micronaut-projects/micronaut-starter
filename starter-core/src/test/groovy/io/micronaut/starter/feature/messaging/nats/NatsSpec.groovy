package io.micronaut.starter.feature.messaging.nats

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class NatsSpec extends BeanContextSpec {

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["nats"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.nats:micronaut-nats")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["nats"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.nats</groupId>
      <artifactId>micronaut-nats</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['nats'])

        then:
        ctx.configuration.containsKey('nats.addresses')
    }
}
