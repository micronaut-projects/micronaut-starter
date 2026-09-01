package io.micronaut.starter.feature.vertx

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VertxClientSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle vertx-mysql-client feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['vertx-mysql-client'])
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-vertx-mysql-client")')

        where:
        language << supportedLanguages(BuildTool.GRADLE)
    }

    @Unroll
    void 'test maven vertx-mysql-client feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['vertx-mysql-client'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.sql", "micronaut-vertx-mysql-client", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test vertx-mysql-client configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['vertx-mysql-client'])

        then:
        commandContext.configuration.get('vertx.mysql.client.port'.toString()) == 3306
        commandContext.configuration.get('vertx.mysql.client.host'.toString()) == 'the-host'
        commandContext.configuration.get('vertx.mysql.client.database'.toString()) == 'the-db'
        commandContext.configuration.get('vertx.mysql.client.database.user'.toString()) == 'user'
        commandContext.configuration.get('vertx.mysql.client.database.password'.toString()) == 'password'
        commandContext.configuration.get('vertx.mysql.client.database.maxSize'.toString()) == 5
    }

}
