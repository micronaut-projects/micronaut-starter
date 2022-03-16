package io.micronaut.starter.feature.rss

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RssItunesSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature rss-itunes-podcast contains links to micronaut docs'() {
        when:
        def output = generate(['rss-itunes-podcast'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rss/latest/guide/index.html#itunespodcast")
    }

    @Unroll
    void 'test gradle rss-itunes-podcast feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['rss-itunes-podcast'])
                .render()

        then:
        template.contains('implementation("io.micronaut.rss:micronaut-itunespodcast")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven rss-itunes-podcast feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['rss-itunes-podcast'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rss</groupId>
      <artifactId>micronaut-itunespodcast</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
