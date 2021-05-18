package io.micronaut.starter.feature.acme

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AcmeSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature acme contains links to micronaut docs'() {
        when:
        def output = generate(['acme'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-acme/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle acme feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['acme'])
                .render()

        then:
        template.contains('implementation("io.micronaut.acme:micronaut-acme")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven acme feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['acme'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.acme</groupId>
      <artifactId>micronaut-acme</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test acme configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['acme'])

        then:
        commandContext.configuration.get('acme.enabled'.toString()) == 'true'
        commandContext.configuration.get('acme.tos-agree'.toString()) == 'true'
        commandContext.configuration.get('micronaut.server.ssl.enabled'.toString()) == 'true'
    }
}
