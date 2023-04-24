package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JAsyncSQLSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature jasync-sql contains links to micronaut and 3rd party docs'() {
        when:
        def output = generate(['jasync-sql','mysql'])
        def readme = output["README.md"]

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
        Map<String, String> output = generate(['yaml', 'jasync-sql','mysql'])
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
        output = generate(['yaml', 'jasync-sql','postgres'])
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
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jasync-sql feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['jasync-sql','mysql'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jasync-sql</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
