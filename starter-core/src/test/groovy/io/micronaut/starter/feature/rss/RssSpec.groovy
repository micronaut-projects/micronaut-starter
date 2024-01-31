package io.micronaut.starter.feature.rss

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RssSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature rss contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['rss'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rss/latest/guide/index.html#whatsNew")
    }

    @Unroll
    void 'test gradle rss feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['rss'])
                .render()

        then:
        template.contains('implementation("io.micronaut.rss:micronaut-rss")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven rss feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['rss'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rss</groupId>
      <artifactId>micronaut-rss</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
