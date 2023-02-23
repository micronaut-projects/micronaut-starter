package io.micronaut.starter.feature.discovery

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class DiscoveryKubernetesSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature discovery-kubernetes contains links to micronaut docs'() {
        when:
        def output = generate(['discovery-kubernetes'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#service-discovery")
    }

    @Unroll
    void 'test gradle discovery-kubernetes feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['discovery-kubernetes'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut:micronaut-discovery-core")')
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-discovery-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven discovery-kubernetes feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['discovery-kubernetes'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-discovery-client</artifactId>
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

    void 'test discovery-kubernetes configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-kubernetes'])

        then:
        commandContext.bootstrapConfiguration.get('kubernetes.client.discovery.mode-configuration.endpoint.watch.enabled'.toString()) == true
        commandContext.bootstrapConfiguration.get('kubernetes.client.discovery.mode') == 'endpoint'
    }

}
