package io.micronaut.starter.feature.jmx

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JmxSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Unroll
    void 'test readme.md contains links to jmx and micronaut docs'() {
        when:
        Map<String, String> output = generate(['jmx'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jmx/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle jmx feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jmx'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.jmx:micronaut-jmx")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jmx feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['jmx'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jmx</groupId>
      <artifactId>micronaut-jmx</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
