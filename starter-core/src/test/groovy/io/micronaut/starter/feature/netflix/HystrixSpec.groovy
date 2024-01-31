package io.micronaut.starter.feature.netflix

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class HystrixSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature netflix-hystrix contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['netflix-hystrix'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#netflixHystrix")
    }

    @Unroll
    void 'test gradle netflix-hystrix feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['netflix-hystrix'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.netflix:micronaut-netflix-hystrix")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven netflix-hystrix feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['netflix-hystrix'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.netflix</groupId>
      <artifactId>micronaut-netflix-hystrix</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test netflix-hystrix configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['netflix-hystrix'])

        then:
        commandContext.configuration.get('hystrix.stream.enabled'.toString()) == false
    }

}
