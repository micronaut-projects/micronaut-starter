package io.micronaut.starter.feature.messaging.jms

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

import java.util.stream.Collectors

class JmsSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test README.md with feature jms contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([feature])
        String readme = output['README.md']

        then:
        readme
        readme.contains 'https://micronaut-projects.github.io/micronaut-jms/snapshot/guide/index.html'

        where:
        feature << beanContext
                .streamOfType(JmsFeature)
                .map { f -> f.name }
                .collect(Collectors.toList())
    }

    void 'test dependencies are present for Gradle'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jms-' + name])
                .render()

        then:
        template.contains """implementation("io.micronaut.jms:micronaut-jms-$name")"""

        where:
        name << ['activemq-artemis', 'activemq-classic', 'sqs', 'core']
    }

    void 'test dependencies are present for Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['jms-' + name])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jms</groupId>
      <artifactId>micronaut-jms-$name</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        name << ['activemq-artemis', 'activemq-classic', 'sqs', 'core']
    }

    void 'test ActiveMQ "Classic" config'() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['jms-activemq-classic'])

        then:
        ctx.configuration.containsKey 'micronaut.jms.activemq.classic.enabled'
        ctx.configuration.containsKey 'micronaut.jms.activemq.classic.connection-string'
    }

    void 'test ActiveMQ Artemis config'() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['jms-activemq-artemis'])

        then:
        ctx.configuration.containsKey 'micronaut.jms.activemq.artemis.enabled'
        ctx.configuration.containsKey 'micronaut.jms.activemq.artemis.connection-string'
    }

    void 'test SQS config'() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['jms-sqs'])

        then:
        ctx.configuration.containsKey 'micronaut.jms.sqs.enabled'
    }
}
