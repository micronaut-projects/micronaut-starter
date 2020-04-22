package io.micronaut.starter.feature.vertx

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VertxClientSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle vertx-mysql-client feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['vertx-mysql-client'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.configuration:micronaut-vertx-mysql-client")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven vertx-mysql-client feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['vertx-mysql-client'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-vertx-mysql-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    void 'test vertx-mysql-client configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['vertx-mysql-client'])

        then:
        commandContext.configuration.get('vertx.mysql.client.port'.toString()) == 5432
        commandContext.configuration.get('vertx.mysql.client.host'.toString()) == 'the-host'
        commandContext.configuration.get('vertx.mysql.client.database'.toString()) == 'the-db'
        commandContext.configuration.get('vertx.mysql.client.database.user'.toString()) == 'user'
        commandContext.configuration.get('vertx.mysql.client.database.password'.toString()) == 'password'
        commandContext.configuration.get('vertx.mysql.client.database.maxSize'.toString()) == 5
    }

}
