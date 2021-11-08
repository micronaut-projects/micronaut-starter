package io.micronaut.starter.feature.micrometer

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class MicrometerSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle micrometer feature #micrometerFeature.name'(MicrometerFeature micrometerFeature) {
        given:
        String dependency = micrometerFeature.getArtifactId()
        String group = micrometerFeature.getGroupId()

        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([micrometerFeature.name])
                .render()

        then:
        template.contains("implementation(\"${group}:${dependency}\")")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    void "test gradle micrometer multiple features"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["micrometer-atlas", "micrometer-influx"])
                .render()

        then:
        template.contains("""
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-atlas")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-influx")
""")
        template.count("io.micronaut.micrometer:micronaut-micrometer-core") == 1
    }

    @Unroll
    void 'test maven micrometer feature #micrometerFeature.name'(MicrometerFeature micrometerFeature) {
        given:
        String dependency = micrometerFeature.getArtifactId()
        String group = micrometerFeature.getGroupId()

        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([micrometerFeature.name])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>$group</groupId>
      <artifactId>$dependency</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    void "test maven micrometer multiple features"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["micrometer-atlas", "micrometer-influx"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>micronaut-micrometer-core</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>micronaut-micrometer-registry-atlas</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>micronaut-micrometer-registry-influx</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.count("micronaut-micrometer-core") == 1
    }

    @Unroll
    void 'test micrometer configuration for feature=#micrometerFeature'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([micrometerFeature])

        then: 'the micrometer configuration is enabled for the feature'
        commandContext.configuration.get("micronaut.metrics.export.${configKey}.enabled".toString()) == true
        commandContext.configuration.get('micronaut.metrics.enabled') == true

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature)*.name.iterator()
        configKey = "${micrometerFeature - 'micrometer-'}".replace('-', '')
    }

    void 'test mandatory dependencies for micrometer features are added'() {
        when: 'a new feature context with one micrometer feature'
        Features features = getFeatures(["micrometer-appoptics"])

        then: 'the micrometer feature is added and also micrometer-core and management'
        features.containsAll(['micrometer-appoptics', 'micrometer', 'management'])
    }

    void 'test title conversion is done properly'() {
        when: 'a feature with one word'
        Atlas atlas = beanContext.getBean(Atlas)

        then: 'the title is correct'
        atlas.title == 'Micrometer Atlas'

        when: 'a feature with two words in the name'
        NewRelic newRelic = beanContext.getBean(NewRelic)

        then: 'the title is correct'
        newRelic.title == 'Micrometer New Relic'
    }

}
