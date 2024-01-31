package io.micronaut.starter.feature.netflix

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RibbonSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature netflix-ribbon contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['netflix-ribbon'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#netflixRibbon")
    }

    @Unroll
    void 'test gradle netflix-ribbon feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['netflix-ribbon'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.netflix:micronaut-netflix-ribbon")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven netflix-ribbon feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['netflix-ribbon'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.netflix</groupId>
      <artifactId>micronaut-netflix-ribbon</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test netflix-ribbon configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['netflix-ribbon'])

        then:
        commandContext.configuration.get('ribbon.VipAddress'.toString()) == 'test'
        commandContext.configuration.get('ribbon.ServerListRefreshInterval'.toString()) == 2000
    }

}
