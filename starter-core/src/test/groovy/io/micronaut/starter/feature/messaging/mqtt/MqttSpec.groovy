package io.micronaut.starter.feature.messaging.mqtt

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

import java.util.stream.Collectors

class MqttSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature mqtt contains links to micronaut docs'() {
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
        template.contains('sharedServer = true') == (dependency == 'mqttv5')
        template.contains('''<artifactId>micronaut-maven-plugin</artifactId>
                            |  <configuration>
                            |    <shared>true</shared>
                            |</configuration>'''.stripMargin()) == (dependency == 'mqttv5')

        where:
        feature  | dependency
        "mqtt"   | "mqttv5"
        "mqttv3" | "mqttv3"
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
        where:
        feature  | dependency
        "mqtt"   | "mqttv5"
        "mqttv3" | "mqttv3"
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([feature])

        then:
        ctx.configuration.containsKey('mqtt.client.server-uri')
        ctx.configuration.containsKey('mqtt.client.client-id')

        where:
        feature << beanContext.streamOfType(MqttFeature)
                             .map{f -> f.getName()}
                             .collect(Collectors.toList())
    }
}
