package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class DataJpaSpec extends BeanContextSpec {

    void "test data jpa features"() {
        when:
        Features features = getFeatures(['data-jpa'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("jdbc-hikari")
        features.contains("data-jpa")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["data-jpa"]), false).render().toString()

        then:
        template.contains("annotationProcessor(\"io.micronaut.data:micronaut-data-processor\")")
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-jpa")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains("runtimeOnly(\"com.h2database:h2\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["data-jpa"]), []).render().toString()

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
      <artifactId>micronaut-data-hibernate-jpa</artifactId>
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
        GeneratorContext ctx = buildGeneratorContext(['data-jpa'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.containsKey("jpa.default.properties.hibernate.hbm2ddl.auto")
        ctx.configuration.get("datasources.default.dialect") == "H2"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'postgres'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "POSTGRES"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'mysql'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'mariadb'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'oracle'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "ORACLE"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'sqlserver'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "SQL_SERVER"
    }
}
