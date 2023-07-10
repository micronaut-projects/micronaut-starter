package io.micronaut.starter.feature.messaging.mqtt

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.testresources.TestResources
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

import java.util.stream.Collectors

class MqttHiveMqSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature mqtt-hivemq contains links to micronaut docs'() {
        when:
        def output = generate([feature])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mqtt/latest/guide/index.html")

        where:
        feature << beanContext.streamOfType(MqttFeature)
                .map{f -> f.getName()}
                .collect(Collectors.toList())
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature])
                .render()

        then:
        template.contains('implementation("io.micronaut.mqtt:micronaut-' + dependency + '")')
        template.contains('id("io.micronaut.test-resources") version') == (dependency == 'mqtt-hivemq')
        template.contains('sharedServer = true') == (dependency == 'mqtt-hivemq')

        where:
        feature         | dependency
        "mqtt-hivemq"   | "mqtt-hivemq"

    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([feature])
                .render()
        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.mqtt</groupId>
      <artifactId>micronaut-${dependency}</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("<$TestResources.MICRONAUT_TEST_RESOURCES_ENABLED>true</$TestResources.MICRONAUT_TEST_RESOURCES_ENABLED>") == (dependency == 'mqtt-hivemq')
        template.contains('''<artifactId>micronaut-maven-plugin</artifactId>
          <configuration>
            <shared>true</shared>
          </configuration>''') == (dependency == 'mqtt-hivemq')

        where:
        feature         | dependency
        "mqtt-hivemq"   | "mqtt-hivemq"

    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([feature])

        then:
        ctx.configuration.containsKey('mqtt.client.server-uri') == (feature == MqttHiveMq.NAME)
        ctx.configuration.containsKey('mqtt.client.client-id')

        where:
        feature << beanContext.streamOfType(MqttHiveMq)
                             .map{f -> f.getName()}
                             .collect(Collectors.toList())
    }
}
