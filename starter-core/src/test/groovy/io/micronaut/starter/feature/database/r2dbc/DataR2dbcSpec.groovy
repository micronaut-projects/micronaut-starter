package io.micronaut.starter.feature.database.r2dbc

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Unroll

class DataR2dbcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    JdbcFeature jdbcFeature = beanContext.getBean(JdbcFeature)

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        def output = generate(['data-r2dbc'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-r2dbc/latest/guide/")
    }

    void "test data r2dbc features"() {
        when:
        Features features = getFeatures(['data-r2dbc'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("r2dbc")
        features.contains("data-r2dbc")
        !features.contains("jdbc-hikari")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-r2dbc"])
                .render()

        then:
        jdbcFeature.name == 'jdbc-hikari'
        template.contains("annotationProcessor(\"io.micronaut.data:micronaut-data-processor\")")
        template.contains('implementation("io.micronaut.r2dbc:micronaut-data-r2dbc")')
        template.contains('implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")')
        template.contains("runtimeOnly(\"io.r2dbc:r2dbc-h2\")")
        !template.contains("implementation(\"io.micronaut.sql:micronaut-jdbc-hikari\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["data-r2dbc"])
                .render()

        then:
        //src/main
        template.contains('''
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
          </annotationProcessorPaths>
''')

        template.contains('''
    <dependency>
      <groupId>io.micronaut.r2dbc</groupId>
      <artifactId>micronaut-data-r2dbc</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''
    <dependency>
      <groupId>io.micronaut.r2dbc</groupId>
      <artifactId>micronaut-r2dbc-core</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''
    <dependency>
      <groupId>io.r2dbc</groupId>
      <artifactId>r2dbc-h2</artifactId>
      <scope>runtime</scope>
    </dependency>
''')
        jdbcFeature.name == 'jdbc-hikari'
        !template.contains('''
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
''')

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()
    }

    @Unroll
    void "test config #driver and #dialect"() {
        given:
        GeneratorContext ctx = buildGeneratorContext(['data-r2dbc', driver])

        expect:
        ctx.configuration.containsKey("r2dbc.datasources.default.url")
        ctx.configuration.get("r2dbc.datasources.default.dialect") == dialect

        where:
        driver      | dialect
        "h2"        | "H2"
        "postgres"  | "POSTGRES"
        "mysql"     | "MYSQL"
        "mariadb"   | "MYSQL"
        "sqlserver" | "SQL_SERVER"
    }
}
