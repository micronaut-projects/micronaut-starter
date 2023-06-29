package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class FreemarkerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-freemarker contains links to micronaut docs'() {
        when:
        def output = generate(['views-freemarker'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://freemarker.apache.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#freemarker")
    }

    @Unroll
    void 'test gradle views-freemarker feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['views-freemarker'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-freemarker")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-freemarker feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-freemarker'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-freemarker</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
