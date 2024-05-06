package io.micronaut.starter.feature.messaging.mqtt

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.testresources.TestResources
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

import java.util.stream.Collectors

class MqttSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature mqtt contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([feature])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mqtt/latest/guide/index.html")

        where:
        feature << beanContext.streamOfType(MqttFeature)
                .map {f -> f.getName() }
                .collect(Collectors.toList())
    }

    void "test dependencies are present for #buildTool"(BuildTool buildTool, String feature, String dependency, boolean hasTestResources) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([feature])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.mqtt", "micronaut-${dependency}", Scope.COMPILE)

        if (hasTestResources) {
            if (buildTool.isGradle()) {
                assert verifier.hasBuildPlugin("io.micronaut.test-resources")
            } else if (buildTool == BuildTool.MAVEN) {
                assert template.contains("<$TestResources.MICRONAUT_TEST_RESOURCES_ENABLED>true</$TestResources.MICRONAUT_TEST_RESOURCES_ENABLED>")
                template.contains('''\
    <plugins>
      <plugin>
        <groupId>io.micronaut.maven</groupId>
        <artifactId>micronaut-maven-plugin</artifactId>
        <configuration>
          <shared>true</shared>
''')
            }
        }

        where:
        buildTool               | feature       | dependency | hasTestResources
        BuildTool.GRADLE        | "mqtt-hivemq" | "mqtt-hivemq" | true
        BuildTool.GRADLE_KOTLIN | "mqtt-hivemq" | "mqtt-hivemq" | true
        BuildTool.MAVEN         | "mqtt-hivemq" | "mqtt-hivemq" | true
        BuildTool.GRADLE        | "mqtt"        | "mqttv5"   | true
        BuildTool.GRADLE_KOTLIN | "mqtt"        | "mqttv5"   | true
        BuildTool.MAVEN         | "mqtt"        | "mqttv5"   | true
        BuildTool.GRADLE        | "mqttv3"      | "mqttv3"   | false
        BuildTool.GRADLE_KOTLIN | "mqttv3"      | "mqttv3"   | false
        BuildTool.MAVEN         | "mqttv3"      | "mqttv3"   | false
    }

    void "test config"(String feature) {
        when:
        GeneratorContext ctx = buildGeneratorContext([feature])

        then:
        ctx.configuration.containsKey('mqtt.client.server-uri') == (feature == MqttV3.NAME)
        ctx.configuration.containsKey('mqtt.client.client-id')

        where:
        feature << beanContext.streamOfType(MqttFeature)
                             .map{f -> f.getName()}
                             .collect(Collectors.toList())
    }
}
