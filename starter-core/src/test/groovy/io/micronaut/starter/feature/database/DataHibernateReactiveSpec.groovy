package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Issue

class DataHibernateReactiveSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test data jpa reactive requires db"() {
        when:
        Features features = getFeatures([DataHibernateReactive.NAME])

        then:
        def exception = thrown(IllegalArgumentException)

        and:
        exception.message == "$DataHibernateReactive.NAME requires $MySQL.NAME, $MariaDB.NAME, $PostgreSQL.NAME, $Oracle.NAME, or $SQLServer.NAME"
    }

    void "test data jpa reactive features for #db"() {
        when:
        Features features = getFeatures([DataHibernateReactive.NAME, db])

        then:
        features.contains("data")
        features.contains(db)
        !features.contains("jdbc-hikari")
        features.contains(DataHibernateReactive.NAME)

        where:
        db << [MySQL.NAME, MariaDB.NAME, PostgreSQL.NAME, Oracle.NAME, SQLServer.NAME]
    }

    void "test dependencies are present for gradle with #db (#client)"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataHibernateReactive.NAME, db])
                .render()

        then:
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-jpa")')
        template.contains('implementation("io.micronaut.sql:micronaut-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains($/implementation("$DataHibernateReactive.IO_VERTX_DEPENDENCY_GROUP:$client")/$)

        where:
        db              | client
        MySQL.NAME      | 'vertx-mysql-client'
        MariaDB.NAME    | 'vertx-mysql-client'
        PostgreSQL.NAME | 'vertx-pg-client'
        Oracle.NAME     | 'vertx-oracle-client'
        SQLServer.NAME  | 'vertx-mssql-client'
    }

    void "test kotlin jpa plugin is present for gradle kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('id("org.jetbrains.kotlin.plugin.jpa")')
    }

    void "test dependencies are present for maven with #db (#client)"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, db])
                .render()

        then:
        //src/main
        template.contains('''\
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
''')
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-hibernate-reactive</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        !template.contains('''\
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains("""\
    <dependency>
      <groupId>$DataHibernateReactive.IO_VERTX_DEPENDENCY_GROUP</groupId>
      <artifactId>$client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        db              | client
        MySQL.NAME      | 'vertx-mysql-client'
        MariaDB.NAME    | 'vertx-mysql-client'
        PostgreSQL.NAME | 'vertx-pg-client'
        Oracle.NAME     | 'vertx-oracle-client'
        SQLServer.NAME  | 'vertx-mssql-client'
    }

    void "test kotlin jpa plugin is present for maven kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .language(Language.KOTLIN)
                .render()

        then:
        //src/main
        template.contains('''
          <compilerPlugins>
            <plugin>jpa</plugin>
            <plugin>all-open</plugin>
          </compilerPlugins>
''')
    }

    void "test config for #db"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([DataHibernateReactive.NAME, db])

        then:
        !ctx.configuration.containsKey("datasources.default.url")
        !ctx.configuration.containsKey("datasources.default.username")
        !ctx.configuration.containsKey("datasources.default.password")
        ctx.configuration."datasources.default.dialect" == dialect
        ctx.configuration."jpa.default.reactive" == true
        with(ctx.configuration."jpa.default.properties.hibernate.connection.url") {
            it.startsWith("jdbc:")
            it.contains(jdbcContains)
        }
        ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.username")
        ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.password")

        where:
        db              | dialect      | jdbcContains
        MySQL.NAME      | 'MYSQL'      | 'mysql'
        MariaDB.NAME    | 'MYSQL'      | 'mariadb'
        PostgreSQL.NAME | 'POSTGRES'   | 'postgresql'
        Oracle.NAME     | 'ORACLE'     | 'oracle'
        SQLServer.NAME  | 'SQL_SERVER' | 'sqlserver'
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/686")
    void 'test data-processor dependency is in provided scope for Groovy and Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .render()

        then:
        template.contains('''
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-processor</artifactId>
      <scope>provided</scope>
    </dependency>
''')
    }
}
