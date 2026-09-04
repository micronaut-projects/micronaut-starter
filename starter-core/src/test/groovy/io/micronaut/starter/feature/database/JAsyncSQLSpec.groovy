package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Unroll

class JAsyncSQLSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature jasync-sql contains links to micronaut and 3rd party docs'() {
        when:
        Map<String, String> output = generate(['jasync-sql','mysql'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jasync")
        readme.contains("https://github.com/jasync-sql/jasync-sql/wiki")
    }

    void 'validation fails for jasync-sql if missing exactly one of mysql or postgress feature'() {
        when:
        generate(['jasync-sql'])

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "jasync-sql requires either mysql or postgres"

        when:
        generate(['jasync-sql','mysql','postgres'])

        then:
        e = thrown(IllegalArgumentException)
        e.message.startsWith("There can only be one of the following features selected:")
    }

    void 'configuration includes defaults for feature jasync-sql'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'jasync-sql', 'mysql'])
        String configuration = output["src/main/resources/application.yml"]

        then:
        configuration.contains("""
jasync:
  client:
    port: 5432
    host: the-host
    database: the-db
    username: test
    password: test
    maxActiveConnections: 5
""")

        when:
        output = generate([Yaml.NAME, 'jasync-sql','postgres'])
        configuration = output["src/main/resources/application.yml"]

        then:
        configuration.contains("""
jasync:
  client:
    port: 5432
    host: the-host
    database: the-db
    username: test
    password: test
    maxActiveConnections: 5
""")

    }

    @Unroll
    void 'test gradle jasync-sql feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jasync-sql','mysql'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jasync-sql")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven jasync-sql feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['jasync-sql','mysql'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.sql", "micronaut-jasync-sql", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }
}
