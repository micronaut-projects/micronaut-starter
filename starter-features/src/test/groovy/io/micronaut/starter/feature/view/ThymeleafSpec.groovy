package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ThymeleafSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-thymeleaf contains links to micronaut docs'() {
        when:
        def output = generate(['views-thymeleaf'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://www.thymeleaf.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#thymeleaf")
    }

    @Unroll
    void 'test gradle views-thymeleaf feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['views-thymeleaf'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-thymeleaf")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-thymeleaf feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-thymeleaf'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-thymeleaf</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
