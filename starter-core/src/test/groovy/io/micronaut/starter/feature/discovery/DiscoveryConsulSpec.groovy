package io.micronaut.starter.feature.discovery

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Issue
import spock.lang.Unroll

class DiscoveryConsulSpec extends ApplicationContextSpec  implements CommandOutputFixture {

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
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['discovery-consul'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut:micronaut-discovery-core")')
        template.contains('implementation("io.micronaut.discovery:micronaut-discovery-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven discovery-consul feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['discovery-consul'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.discovery</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-discovery-core</artifactId>
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
