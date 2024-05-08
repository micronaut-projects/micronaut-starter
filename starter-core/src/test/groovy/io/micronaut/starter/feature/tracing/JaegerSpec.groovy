package io.micronaut.starter.feature.tracing

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

class JaegerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature tracing-jaeger contains links to micronaut docs'() {
        when:
        Map output = generate([Jaeger.NAME])
        String readme = output['README.md']

        then:
        readme
        readme.contains 'https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#jaeger'
        readme.contains 'https://www.jaegertracing.io/'
    }

    @Unroll
    void 'test gradle tracing-jaeger feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE)
                .language(language)
                .features([Jaeger.NAME])
                .render()

        then:
        template.contains 'implementation("io.micronaut.tracing:micronaut-tracing-jaeger")'

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven tracing-jaeger feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, MAVEN)
                .language(language)
                .features([Jaeger.NAME])
                .render()

        then:
        template.contains '''
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-jaeger</artifactId>
      <scope>compile</scope>
    </dependency>
'''

        where:
        language << Language.values().toList()
    }

    void 'test tracing-jaeger configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([Jaeger.NAME])

        then:
        commandContext.configuration.get('tracing.jaeger.enabled') == true
        commandContext.configuration.get('tracing.jaeger.sampler.probability') == 0.1
    }
}
