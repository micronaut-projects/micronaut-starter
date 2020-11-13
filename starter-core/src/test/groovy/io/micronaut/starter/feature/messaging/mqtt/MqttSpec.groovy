package io.micronaut.starter.feature.messaging.mqtt

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

import java.util.stream.Collectors

class MqttSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature mqtt contains links to micronaut docs'() {
        when:
        def output = generate(['mqtt'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mqtt/latest/guide/index.html")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([feature]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.mqtt:micronaut-' + dependency + '")')

        where:
        feature  | dependency
        "mqtt"   | "mqttv5"
        "mqttv5" | "mqttv5"
        "mqttv3" | "mqttv3"
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures([feature]), []).render().toString()

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
        "mqttv5" | "mqttv5"
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
