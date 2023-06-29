package io.micronaut.starter.feature.vertx

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VertxPgSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle vertx-pg-client feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['vertx-pg-client'])
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-vertx-pg-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven vertx-pg-client feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['vertx-pg-client'])
                .render()
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
