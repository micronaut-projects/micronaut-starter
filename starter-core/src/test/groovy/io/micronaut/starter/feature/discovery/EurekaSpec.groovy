package io.micronaut.starter.feature.discovery

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class EurekaSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle discovery-eureka feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['discovery-eureka'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-discovery-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven discovery-eureka feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['discovery-eureka'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test discovery-eureka configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-eureka'])

        then:
        commandContext.configuration.get('eureka.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('eureka.client.defaultZone') == '${EUREKA_HOST:localhost}:${EUREKA_PORT:8761}'
    }

}
