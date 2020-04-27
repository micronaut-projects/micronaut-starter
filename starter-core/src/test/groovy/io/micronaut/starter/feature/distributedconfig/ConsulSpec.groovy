package io.micronaut.starter.feature.distributedconfig

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ConsulSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle config-consul feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['config-consul'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-discovery-client")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle config-consul multiple features'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['config-consul', 'discovery-consul'])).render().toString()

        then:
        template.count('implementation("io.micronaut:micronaut-discovery-client")') == 1
    }

    @Unroll
    void 'test maven config-consul feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['config-consul'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test maven config-consul multiple features'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['config-consul', 'discovery-consul']), []).render().toString()

        then:
        template.count("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""") == 1
    }

    void 'test config-consul configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['config-consul'])

        then:
        commandContext.bootstrapConfig.get('micronaut.application.name'.toString()) == 'foo'
        commandContext.bootstrapConfig.get('micronaut.config-client.enabled'.toString()) == true
        commandContext.bootstrapConfig.get('consul.client.registration.enabled'.toString()) == true
        commandContext.bootstrapConfig.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'
    }

}
