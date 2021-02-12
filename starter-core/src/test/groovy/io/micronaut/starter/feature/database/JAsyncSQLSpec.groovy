package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.GradleDsl
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JAsyncSQLSpec extends BeanContextSpec implements CommandOutputFixture {

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
        def output = generate(['jasync-sql','mysql'])
        def configuration = output["src/main/resources/application.yml"]

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
        output = generate(['jasync-sql','postgres'])
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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jasync-sql','mysql'], language), new GradleBuild()).render().toString()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jasync-sql")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jasync-sql feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jasync-sql','mysql'], language), new MavenBuild()).render().toString()

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
