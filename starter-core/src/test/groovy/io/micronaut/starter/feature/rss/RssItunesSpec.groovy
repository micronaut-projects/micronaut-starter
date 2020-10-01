package io.micronaut.starter.feature.rss

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RssItunesSpec extends BeanContextSpec implements CommandOutputFixture {

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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['rss-itunes-podcast'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.rss:micronaut-itunespodcast")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven rss-itunes-podcast feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['rss-itunes-podcast'], language), []).render().toString()

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
