package io.micronaut.starter.feature.tracing

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

class ZipkinSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature tracing-zipkin contains links to micronaut docs'() {
        when:
        Map output = generate(['tracing-zipkin'])
        String readme = output['README.md']

        then:
        readme
        readme.contains 'https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#zipkin'
        readme.contains 'https://zipkin.io/'
    }

    @Unroll
    void 'test gradle tracing-zipkin feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE)
                .language(language)
                .features(['tracing-zipkin'])
                .render()

        then:
        template.contains 'implementation("io.micronaut.tracing:micronaut-tracing-zipkin")'

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven tracing-zipkin feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, MAVEN)
                .language(language)
                .features(['tracing-zipkin'])
                .render()

        then:
        template.contains '''
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-zipkin</artifactId>
      <scope>compile</scope>
    </dependency>
'''

        where:
        language << Language.values().toList()
    }

    void 'test tracing-zipkin configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['tracing-zipkin'])

        then:
        commandContext.configuration.get('tracing.zipkin.enabled') == true
        commandContext.configuration.get('tracing.zipkin.http.url') == 'http://localhost:9411'
        commandContext.configuration.get('tracing.zipkin.sampler.probability') == 0.1
    }
}
