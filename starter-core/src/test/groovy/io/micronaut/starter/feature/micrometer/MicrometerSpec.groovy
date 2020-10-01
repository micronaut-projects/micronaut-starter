package io.micronaut.starter.feature.micrometer

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class MicrometerSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle micrometer feature #micrometerFeature.name'() {
        given:
        String dependency = "micronaut-micrometer-registry-${micrometerFeature.name - 'micrometer-'}"
        Features features = getFeatures([micrometerFeature.name])

        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), features, false).render().toString()

        then:
        template.contains("implementation(\"io.micronaut.micrometer:${dependency}\")")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    void "test gradle micrometer multiple features"() {
        when:
        Features features = getFeatures(["micrometer-atlas", "micrometer-influx"])
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), features, false).render().toString()

        then:
        template.contains("""
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-atlas")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-influx")
""")
        template.count("io.micronaut.micrometer:micronaut-micrometer-core") == 1
    }

    @Unroll
    void 'test maven micrometer feature #micrometerFeature.name'() {
        given:
        String dependency = "micronaut-micrometer-registry-${micrometerFeature.name - 'micrometer-'}"
        Features features = getFeatures([micrometerFeature.name], null, null, BuildTool.MAVEN)

        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.micrometer</groupId>
      <artifactId>$dependency</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature).iterator()
    }

    void "test maven micrometer multiple features"() {
        when:
        Features features = getFeatures(["micrometer-atlas", "micrometer-influx"], null, null, BuildTool.MAVEN)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

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
