package io.micronaut.starter.feature.rss

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RssSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature rss contains links to micronaut docs'() {
        when:
        def output = generate(['rss'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rss/latest/guide/index.html#whatsNew")
    }

    @Unroll
    void 'test gradle rss feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['rss'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.rss:micronaut-rss")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven rss feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['rss'], language), []).render().toString()

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
