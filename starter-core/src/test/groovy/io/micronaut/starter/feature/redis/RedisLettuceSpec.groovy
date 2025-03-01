package io.micronaut.starter.feature.redis

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
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

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.redis</groupId>
      <artifactId>micronaut-redis-lettuce</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test redis-lettuce configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['redis-lettuce'])

        then:
        commandContext.configuration.get('redis.uri'.toString()) == 'redis://localhost'
    }

}
