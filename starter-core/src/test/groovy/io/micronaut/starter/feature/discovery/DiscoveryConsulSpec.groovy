package io.micronaut.starter.feature.discovery

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Issue
import spock.lang.Unroll

class DiscoveryConsulSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature discovery-consul contains links to micronaut docs'() {
        when:
        def output = generate(['discovery-consul'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.consul.io")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#serviceDiscoveryConsul")
    }

    @Unroll
    void 'test gradle discovery-consul feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['discovery-consul'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-discovery-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven discovery-consul feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['discovery-consul'], language), []).render().toString()

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

    void 'test discovery-consul configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-consul'])

        then:
        commandContext.configuration.get('consul.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/508")
    void 'discovery-consul with config-consul only adds configuration to bootstrap.yml'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-consul', 'config-consul'])

        then:
        commandContext.configuration.get('consul.client.registration.enabled'.toString()) == true
        commandContext.bootstrapConfiguration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'

        !commandContext.bootstrapConfiguration.containsKey('consul.client.registration.enabled')
        !commandContext.configuration.containsKey('consul.client.defaultZone')
    }

}
