package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.migration.Flyway
import io.micronaut.starter.feature.migration.Liquibase
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import spock.lang.Issue
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class DataHibernateReactiveSpec extends BaseHibernateReactiveSpec {

    void "test data jpa reactive requires db"() {
        when:
        getFeatures([DataHibernateReactive.NAME])

        then:
        IllegalArgumentException exception = thrown()

        and:
        exception.message == "Hibernate Reactive requires $MariaDB.NAME, $MySQL.NAME, $Oracle.NAME, $PostgreSQL.NAME, or $SQLServer.NAME"
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

    void "test data jpa reactive features with #migration for #db"(String db, String migration) {
        when:
        Features features = getFeatures([DataHibernateReactive.NAME, db, migration])

        then:
        features.contains("data")
        features.contains(db)
        features.contains(migration)
        features.contains("jdbc-hikari")
        features.contains(DataHibernateReactive.NAME)

        where:
        [db, migration] << [[MySQL.NAME, MariaDB.NAME, PostgreSQL.NAME, Oracle.NAME, SQLServer.NAME], [Liquibase.NAME, Flyway.NAME]].combinations()
    }

    void "data hibernate reactive requires java 11"() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_8)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .render()

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "Hibernate Reactive requires at least JDK 11"

        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_11)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .render()

        then:
        noExceptionThrown()

        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_17)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .render()

        then:
        noExceptionThrown()
    }

    void "test dependencies are present for gradle with #db (#client)"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_11)
                .features([DataHibernateReactive.NAME, db])
                .render()

        then:
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains($/implementation("$HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP:$client")/$)

        template.contains('testImplementation("org.testcontainers:testcontainers")')
        template.contains($/testImplementation("org.testcontainers:$container")/$)

        where:
        db              | client                                    | container
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | 'mysql'
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | 'mariadb'
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT                | 'postgresql'
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT                | 'oracle-xe'
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT              | 'mssqlserver'
    }

    void "test dependencies are present for gradle with #db (#client) and #migration"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_11)
                .features([DataHibernateReactive.NAME, db, migration])
                .render()

        then:
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-hibernate-reactive")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains($/implementation("io.micronaut.$migration:micronaut-$migration")/$)
        template.contains($/runtimeOnly("$migrationDriver.groupId:$migrationDriver.artifactId")/$)
        template.contains('runtimeOnly("org.flywaydb:flyway-mysql")') == (migration == Flyway.NAME && db in [MySQL.NAME, MariaDB.NAME])
        template.contains($/implementation("$HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP:$client")/$)

        template.contains('testImplementation("org.testcontainers:testcontainers")')
        template.contains($/testImplementation("org.testcontainers:$container")/$)

        where:
        db              | client                                    | container     | migration      | migrationDriver
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | 'mysql'       | Liquibase.NAME | MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | 'mariadb'     | Liquibase.NAME | MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT                | 'postgresql'  | Liquibase.NAME | PostgreSQL.DEPENDENCY_POSTGRESQL.build()
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT                | 'oracle-xe'   | Liquibase.NAME | Oracle.DEPENDENCY_OJDBC8.build()
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT              | 'mssqlserver' | Liquibase.NAME | SQLServer.DEPENDENCY_MSSQL_JDBC.build()
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | 'mysql'       | Flyway.NAME    | MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | 'mariadb'     | Flyway.NAME    | MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT                | 'postgresql'  | Flyway.NAME    | PostgreSQL.DEPENDENCY_POSTGRESQL.build()
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT                | 'oracle-xe'   | Flyway.NAME    | Oracle.DEPENDENCY_OJDBC8.build()
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT              | 'mssqlserver' | Flyway.NAME    | SQLServer.DEPENDENCY_MSSQL_JDBC.build()
    }

    void "test kotlin jpa plugin is present for gradle kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .jdkVersion(JdkVersion.JDK_11)
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('id("org.jetbrains.kotlin.plugin.jpa")')
    }

    void "test dependencies are present for maven with #db (#client)"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, db])
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        containsDataProcessor(template)
        !containsDataHibernateJpa(template)
        containsDataHibernateReactive(template)
        !containsHikariDependency(template)
        containsVertXDriver(template, client)
        !containsJdbcDriver(template, migrationDriver)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        db              | client                                    | migrationDriver
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT                | PostgreSQL.DEPENDENCY_POSTGRESQL.build()
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT                | Oracle.DEPENDENCY_OJDBC8.build()
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT              | SQLServer.DEPENDENCY_MSSQL_JDBC.build()
    }

    void "test dependencies are present for maven with #db (#client) and #migration"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, db, migration])
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        containsDataProcessor(template)
        !containsDataHibernateJpa(template)
        containsDataHibernateReactive(template)
        containsHikariDependency(template)
        containsVertXDriver(template, client)
        containsMigrationLibrary(template, migration)
        containsJdbcDriver(template, migrationDriver)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        db              | client                                    | migration      | migrationDriver
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | Liquibase.NAME | MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | Liquibase.NAME | MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT                | Liquibase.NAME | PostgreSQL.DEPENDENCY_POSTGRESQL.build()
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT                | Liquibase.NAME | Oracle.DEPENDENCY_OJDBC8.build()
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT              | Liquibase.NAME | SQLServer.DEPENDENCY_MSSQL_JDBC.build()
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | Flyway.NAME    | MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT | Flyway.NAME    | MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT                | Flyway.NAME    | PostgreSQL.DEPENDENCY_POSTGRESQL.build()
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT                | Flyway.NAME    | Oracle.DEPENDENCY_OJDBC8.build()
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT              | Flyway.NAME    | SQLServer.DEPENDENCY_MSSQL_JDBC.build()
    }

    void "test kotlin jpa plugin is present for maven kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .jdkVersion(JdkVersion.JDK_11)
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
        !ctx.configuration.containsKey("datasources.default.dialect")
        ctx.configuration."jpa.default.reactive" == true
        with(ctx.configuration."jpa.default.properties.hibernate.connection.url") {
            it.startsWith("jdbc:")
            it.contains(jdbcContains)
        }
        ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.username")
        ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.password")

        where:
        db              | jdbcContains
        MySQL.NAME      | 'mysql'
        MariaDB.NAME    | 'mariadb'
        PostgreSQL.NAME | 'postgresql'
        Oracle.NAME     | 'oracle'
        SQLServer.NAME  | 'sqlserver'
    }

    void "test config for #db with #migration"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([DataHibernateReactive.NAME, db, migration])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.containsKey("datasources.default.username")
        ctx.configuration.containsKey("datasources.default.password")
        !ctx.configuration.containsKey("datasources.default.dialect")
        ctx.configuration."jpa.default.reactive" == true
        with(ctx.configuration."jpa.default.properties.hibernate.connection.url") {
            it.startsWith("jdbc:")
            it.contains(jdbcContains)
        }
        ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.username")
        ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.password")

        where:
        db              | jdbcContains | migration
        MySQL.NAME      | 'mysql'      | Flyway.NAME
        MariaDB.NAME    | 'mariadb'    | Flyway.NAME
        PostgreSQL.NAME | 'postgresql' | Flyway.NAME
        Oracle.NAME     | 'oracle'     | Flyway.NAME
        SQLServer.NAME  | 'sqlserver'  | Flyway.NAME
        MySQL.NAME      | 'mysql'      | Liquibase.NAME
        MariaDB.NAME    | 'mariadb'    | Liquibase.NAME
        PostgreSQL.NAME | 'postgresql' | Liquibase.NAME
        Oracle.NAME     | 'oracle'     | Liquibase.NAME
        SQLServer.NAME  | 'sqlserver'  | Liquibase.NAME
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/686")
    void 'test data-processor dependency is in provided scope for Groovy and Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .jdkVersion(JdkVersion.JDK_11)
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
