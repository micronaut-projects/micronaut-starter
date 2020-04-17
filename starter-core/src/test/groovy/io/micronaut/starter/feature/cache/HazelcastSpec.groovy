package io.micronaut.starter.feature.cache

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

class HazelcastSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle cache-hazelcast feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['cache-hazelcast'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.cache:micronaut-cache-hazelcast"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven cache-hazelcast feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['cache-hazelcast'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-hazelcast</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test cache-hazelcast configuration'() {
        when:
        CommandContext commandContext = buildCommandContext(['cache-hazelcast'])

        then:
        commandContext.configuration.get('hazelcast.network.addresses'.toString()) == "['121.0.0.1:5701']"
    }

}
