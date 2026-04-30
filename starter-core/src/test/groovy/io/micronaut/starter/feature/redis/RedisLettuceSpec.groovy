package io.micronaut.starter.feature.redis

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RedisLettuceSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle redis-lettuce feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['redis-lettuce'])
                .render()

        then:
        template.contains('implementation("io.micronaut.redis:micronaut-redis-lettuce")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven redis-lettuce feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['redis-lettuce'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.redis", "micronaut-redis-lettuce", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test redis-lettuce configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['redis-lettuce'])

        then:
        commandContext.configuration.get('redis.uri'.toString()) == 'redis://localhost'
    }

}
