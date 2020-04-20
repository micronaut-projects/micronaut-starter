package io.micronaut.starter.feature.postgres

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class PostgresReactiveSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle postgres-reactive feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['postgres-reactive'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-postgres-reactive"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven postgres-reactive feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['postgres-reactive'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-postgres-reactive</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test postgres-reactive configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['postgres-reactive'])

        then:
        commandContext.configuration.get('postgres.reactive.client.port'.toString()) == 5432
        commandContext.configuration.get('postgres.reactive.client.host'.toString()) == 'localhost'
        commandContext.configuration.get('postgres.reactive.client.database'.toString()) == commandContext.getProject().name
        commandContext.configuration.get('postgres.reactive.client.database.user'.toString()) == 'user'
        commandContext.configuration.get('postgres.reactive.client.database.password'.toString()) == 'password'
        commandContext.configuration.get('postgres.reactive.client.database.maxSize'.toString()) == 5
    }

}
