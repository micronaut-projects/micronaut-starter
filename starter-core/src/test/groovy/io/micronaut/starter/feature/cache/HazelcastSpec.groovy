package io.micronaut.starter.feature.cache

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

class HazelcastSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test readme.md contains links to hazelcast and micronaut docs'() {
        when:
        Map<String, String> output = generate(['cache-hazelcast'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://hazelcast.org/")
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html#hazelcast")
    }

    @Unroll
    void 'test gradle cache-hazelcast feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cache-hazelcast'])
                .render()

        then:
        template.contains('implementation("io.micronaut.cache:micronaut-cache-hazelcast")')

        where:
        language << supportedLanguages(BuildTool.GRADLE)
    }

    @Unroll
    void 'test maven cache-hazelcast feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cache-hazelcast'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.cache", "micronaut-cache-hazelcast", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test cache-hazelcast configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cache-hazelcast'])

        then:
        commandContext.configuration.get('hazelcast.network.addresses'.toString()) == "['121.0.0.1:5701']"
    }

}
