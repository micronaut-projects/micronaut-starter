package io.micronaut.starter.feature.discovery

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DiscoveryEurekaSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle discovery-eureka feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['discovery-eureka'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut:micronaut-discovery-client"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven discovery-eureka feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['discovery-eureka'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test discovery-eureka configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['discovery-eureka'])

        then:
        commandContext.configuration.get('eureka.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('eureka.client.defaultZone') == '${EUREKA_HOST:localhost}:${EUREKA_PORT:8761}'
    }

}
