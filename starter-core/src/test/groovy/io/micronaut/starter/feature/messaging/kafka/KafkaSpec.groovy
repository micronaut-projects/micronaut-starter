package io.micronaut.starter.feature.messaging.kafka

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class KafkaSpec extends ApplicationContextSpec implements CommandOutputFixture {

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
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kafka'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka")')
        template.contains('sharedServer = true')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["kafka"])
                .render()
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
        !ctx.configuration.containsKey('kafka.bootstrap.servers')
    }
}
