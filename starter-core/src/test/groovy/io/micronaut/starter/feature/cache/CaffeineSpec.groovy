package io.micronaut.starter.feature.cache

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CaffeineSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature cache-caffeine contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['cache-caffeine'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/ben-manes/caffeine")
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle cache-caffeine feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cache-caffeine'])
                .render()

        then:
        template.contains('implementation("io.micronaut.cache:micronaut-cache-caffeine")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven cache-caffeine feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cache-caffeine'])
                .render()
        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-caffeine</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
