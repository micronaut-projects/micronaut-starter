package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

class DataJdbcSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        def output = generate(['data-jdbc'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc")
    }

    void "test data jdbc features"() {
        when:
        Features features = getFeatures(['data-jdbc'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("jdbc-hikari")
        features.contains("data-jdbc")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["data-jdbc"]), false).render().toString()

        then:
        template.contains("annotationProcessor(\"io.micronaut.data:micronaut-data-processor\")")
        template.contains('implementation("io.micronaut.data:micronaut-data-jdbc")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains("runtimeOnly(\"com.h2database:h2\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["data-jdbc"]), []).render().toString()

        then:
        //src/main
        template.contains("""
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>\${micronaut.data.version}</version>
            </path>
""")
        //src/test
        template.contains("""
                <path>
                  <groupId>io.micronaut.data</groupId>
                  <artifactId>micronaut-data-processor</artifactId>
                  <version>\${micronaut.data.version}</version>
                </path>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-jdbc</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['data-jdbc'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "H2"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'postgres'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "POSTGRES"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'mysql'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'mariadb'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'oracle'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "ORACLE"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'sqlserver'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "SQL_SERVER"
    }
}
