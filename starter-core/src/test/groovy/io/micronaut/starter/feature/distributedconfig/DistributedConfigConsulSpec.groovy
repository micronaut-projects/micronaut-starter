package io.micronaut.starter.feature.distributedconfig

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class DistributedConfigConsulSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature discovery-consul contains links to micronaut docs'() {
        when:
        def output = generate(['config-consul'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.consul.io")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#distributedConfigurationConsul")
    }

    @Unroll
    void 'test gradle config-consul feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['config-consul'])
                .language(language)
                .render()

        then:
        template.count('implementation("io.micronaut.discovery:micronaut-discovery-client")') == 1

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'gradle with features config-consul and discovery-consul adds micronaut-discovery-client only once for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['config-consul', 'discovery-consul'])
                .language(language)
                .render()

        then:
        template.count('implementation("io.micronaut.discovery:micronaut-discovery-client")') == 1

        where:
        language << Language.values().toList()
    }

    void 'test gradle config-consul multiple features'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['config-consul', 'discovery-consul'])
                .render()

        then:
        template.count('implementation("io.micronaut.discovery:micronaut-discovery-client")') == 1
    }

    @Unroll
    void 'test maven config-consul feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['config-consul'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.discovery</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test maven config-consul multiple features'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['config-consul', 'discovery-consul'])
                .render()

        then:
        template.count("""
    <dependency>
      <groupId>io.micronaut.discovery</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""") == 1
    }

    void 'test config-consul configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['config-consul'])

        then:
        commandContext.bootstrapConfiguration.get('micronaut.application.name'.toString()) == 'foo'
        commandContext.bootstrapConfiguration.get('micronaut.config-client.enabled'.toString()) == true
        commandContext.bootstrapConfiguration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'
    }

}
