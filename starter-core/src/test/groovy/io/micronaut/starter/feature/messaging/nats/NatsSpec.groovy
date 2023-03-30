package io.micronaut.starter.feature.messaging.nats

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.BuildTool

class NatsSpec extends ApplicationContextSpec {

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["nats"])
                .render()

        then:
        template.contains('implementation("io.micronaut.nats:micronaut-nats")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["nats"])
                .render()

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
        ctx.configuration.containsKey('nats.default.addresses')
    }
}
