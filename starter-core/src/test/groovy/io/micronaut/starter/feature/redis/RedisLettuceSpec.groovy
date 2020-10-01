package io.micronaut.starter.feature.redis

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RedisLettuceSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle redis-lettuce feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['redis-lettuce'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.redis:micronaut-redis-lettuce")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven redis-lettuce feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['redis-lettuce'], language), []).render().toString()

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
