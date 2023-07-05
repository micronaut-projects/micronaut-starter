package io.micronaut.starter.feature.tracing

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

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
    void 'test gradle tracing-zipkin feature for language=#language'(Language language, BuildTool buildTool) {
        given:
        String feature = 'tracing-zipkin'
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([feature])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-brave-http", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.valuesGradle()].combinations()
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
