package io.micronaut.starter.feature.discovery

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class DiscoveryConsulSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle discovery-consul feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['discovery-consul'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut:micronaut-discovery-client"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven discovery-consul feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['discovery-consul'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    void 'test discovery-consul configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-consul'])

        then:
        commandContext.configuration.get('consul.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'
    }

}
