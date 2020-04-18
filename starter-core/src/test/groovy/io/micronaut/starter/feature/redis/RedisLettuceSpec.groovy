package io.micronaut.starter.feature.redis

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RedisLettuceSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle redis-lettuce feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['redis-lettuce'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-redis-lettuce"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven redis-lettuce feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['redis-lettuce'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-redis-lettuce</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test redis-lettuce configuration'() {
        when:
        CommandContext commandContext = buildCommandContext(['redis-lettuce'])

        then:
        commandContext.configuration.get('redis.uri'.toString()) == 'redis://localhost'
    }

}
