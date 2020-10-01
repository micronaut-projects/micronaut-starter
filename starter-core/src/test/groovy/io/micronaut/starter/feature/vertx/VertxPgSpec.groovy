package io.micronaut.starter.feature.vertx

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VertxPgSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle vertx-pg-client feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['vertx-pg-client'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-vertx-pg-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven vertx-pg-client feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['vertx-pg-client'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-vertx-pg-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test vertx-pg-client configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['vertx-pg-client'])

        then:
        commandContext.configuration.get('vertx.pg.client.port'.toString()) == 5432
        commandContext.configuration.get('vertx.pg.client.host'.toString()) == 'the-host'
        commandContext.configuration.get('vertx.pg.client.database'.toString()) == 'the-db'
        commandContext.configuration.get('vertx.pg.client.database.user'.toString()) == 'user'
        commandContext.configuration.get('vertx.pg.client.database.password'.toString()) == 'password'
        commandContext.configuration.get('vertx.pg.client.database.maxSize'.toString()) == 5
    }

}
