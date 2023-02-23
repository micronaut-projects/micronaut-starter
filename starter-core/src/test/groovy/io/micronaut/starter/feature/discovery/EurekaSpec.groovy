package io.micronaut.starter.feature.discovery

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class EurekaSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature discovery-eureka contains links to micronaut docs'() {
        when:
        def output = generate(['discovery-eureka'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#serviceDiscoveryEureka")
    }

    @Unroll
    void 'test gradle discovery-eureka feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['discovery-eureka'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut:micronaut-discovery-core")')
        template.contains('implementation("io.micronaut.discovery:micronaut-discovery-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven discovery-eureka feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['discovery-eureka'])
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

    void 'test discovery-eureka configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-eureka'])

        then:
        commandContext.configuration.get('eureka.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('eureka.client.defaultZone') == '${EUREKA_HOST:localhost}:${EUREKA_PORT:8761}'
    }

}
