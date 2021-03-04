package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VelocitySpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-velocity contains links to micronaut docs'() {
        when:
        def output = generate(['views-velocity'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://velocity.apache.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#velocity")
    }

    @Unroll
    void 'test gradle views-velocity feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['views-velocity'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-velocity")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-velocity feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-velocity'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-velocity</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
