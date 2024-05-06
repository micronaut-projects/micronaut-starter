package io.micronaut.starter.feature.database

import groovy.transform.Canonical
import groovy.xml.XmlSlurper
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Dependency
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.migration.Flyway
import io.micronaut.starter.feature.migration.Liquibase
import io.micronaut.starter.feature.testresources.DbType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import spock.lang.Issue

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.ARTIFACT_ID_MICRONAUT_DATA_PROCESSOR_ARTIFACT
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA

class DataHibernateReactiveSpec extends BaseHibernateReactiveSpec {

    void "test data jpa reactive requires db"() {
        when:
        getFeatures([DataHibernateReactive.NAME])

        then:
        IllegalArgumentException exception = thrown()

        and:
        exception.message == "Hibernate Reactive requires $MariaDB.NAME, $MySQL.NAME, $PostgreSQL.NAME, or $SQLServer.NAME"
    }

    void "test data jpa reactive features for #db"(String db) {
        when:
        Features features = getFeatures([DataHibernateReactive.NAME, db])

        then:
        features.contains("data")
        features.contains(db)
        !features.contains("jdbc-hikari")
        features.contains(DataHibernateReactive.NAME)

        where:
        db << databases()
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
        [db, migration] << [databases(), [Liquibase.NAME, Flyway.NAME]].combinations()
    }

    private List<String> databases() {
        Oracle.COMPATIBLE_WITH_HIBERNATE_REACTIVE
        ? [MySQL.NAME, MariaDB.NAME, PostgreSQL.NAME, Oracle.NAME, SQLServer.NAME]
        : [MySQL.NAME, MariaDB.NAME, PostgreSQL.NAME, SQLServer.NAME]
    }

    void "test dependencies are present for gradle with #testScenario.db (#testScenario.client)"(TestScenario testScenario) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([DataHibernateReactive.NAME, testScenario.db])
                .render()

        then:
        template.contains('id("io.micronaut.test-resources") version')
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains($/implementation("$HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP:$testScenario.client")/$)

        !template.contains('testImplementation("org.testcontainers:testcontainers")')
        !template.contains($/testImplementation("org.testcontainers:$testScenario.container")/$)

        and: "postgres needs another dependency with vert.x"
        template.contains('implementation("com.ongres.scram:client:') == (testScenario.db == PostgreSQL.NAME)

        and: 'the driver and correct module are included for test-resources'
        !template.contains($/runtimeOnly("$testScenario.driver.groupId:$testScenario.driver.artifactId")/$)
        template.contains($/testResourcesService("$testScenario.driver.groupId:$testScenario.driver.artifactId")/$)
        template.contains($/additionalModules.add("$testScenario.testResourcesModule")/$)

        where:
        testScenario << testScenarios()
    }

    private List<TestScenario> testScenarios() {
        filter([
                new TestScenario(MySQL.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT,  'mysql', null, DbType.MYSQL.hibernateReactiveTestResourcesModuleName, MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()),
                new TestScenario(MariaDB.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT, 'mariadb', null, DbType.MARIADB.hibernateReactiveTestResourcesModuleName, MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()),
                new TestScenario(PostgreSQL.NAME, PostgreSQL.VERTX_PG_CLIENT, 'postgresql', null, DbType.POSTGRESQL.hibernateReactiveTestResourcesModuleName, PostgreSQL.DEPENDENCY_POSTGRESQL.build()),
                new TestScenario(Oracle.NAME, Oracle.VERTX_ORACLE_CLIENT, 'oracle-free', null, DbType.ORACLEFREE.hibernateReactiveTestResourcesModuleName, Oracle.DEPENDENCY_OJDBC11.build()),
                new TestScenario(SQLServer.NAME, SQLServer.VERTX_MSSQL_CLIENT, 'mssqlserver', null, DbType.SQLSERVER.hibernateReactiveTestResourcesModuleName, SQLServer.DEPENDENCY_MSSQL_JDBC.build())
        ])
    }

    void "test dependencies are present for gradle with #testScenario.db (#testScenario.client) and #testScenario.migration"(TestScenario testScenario) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([DataHibernateReactive.NAME, testScenario.db, testScenario.migration])
                .render()

        then:
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-reactive")')
        !template.contains('implementation("io.micronaut.sql:micronaut-hibernate-reactive")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains($/implementation("io.micronaut.$testScenario.migration:micronaut-$testScenario.migration")/$)
        template.contains($/runtimeOnly("$testScenario.driver.groupId:$testScenario.driver.artifactId")/$)
        template.contains('runtimeOnly("org.flywaydb:flyway-mysql")') == (testScenario.migration == Flyway.NAME && testScenario.db in [MySQL.NAME, MariaDB.NAME])
        template.contains($/implementation("$HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP:$testScenario.client")/$)

        !template.contains('testImplementation("org.testcontainers:testcontainers")')
        !template.contains($/testImplementation("org.testcontainers:$testScenario.container")/$)

        and: "postgres needs another dependency with vert.x"
        template.contains('implementation("com.ongres.scram:client:') == (testScenario.db == PostgreSQL.NAME)

        and: 'the correct module is included for test-resources'
        !template.contains($/testResourcesService("$testScenario.driver.groupId:$testScenario.driver.artifactId")/$)
        template.contains($/additionalModules.add("$testScenario.testResourcesModule")/$)

        where:
        testScenario << migrationTestScenarios()
    }

    List<TestScenario> migrationTestScenarios() {
        filter([
                new TestScenario(MySQL.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT,  'mysql', Liquibase.NAME, DbType.MYSQL.hibernateReactiveTestResourcesModuleName, MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()),
                new TestScenario(MariaDB.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT, 'mariadb', Liquibase.NAME, DbType.MARIADB.hibernateReactiveTestResourcesModuleName, MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()),
                new TestScenario(PostgreSQL.NAME, PostgreSQL.VERTX_PG_CLIENT, 'postgresql', Liquibase.NAME, DbType.POSTGRESQL.hibernateReactiveTestResourcesModuleName, PostgreSQL.DEPENDENCY_POSTGRESQL.build()),
                new TestScenario(Oracle.NAME, Oracle.VERTX_ORACLE_CLIENT, 'oracle-xe', Liquibase.NAME, DbType.ORACLEFREE.hibernateReactiveTestResourcesModuleName, Oracle.DEPENDENCY_OJDBC11.build()),
                new TestScenario(SQLServer.NAME, SQLServer.VERTX_MSSQL_CLIENT, 'mssqlserver', Liquibase.NAME, DbType.SQLSERVER.hibernateReactiveTestResourcesModuleName, SQLServer.DEPENDENCY_MSSQL_JDBC.build()),
                new TestScenario(MySQL.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT, 'mysql', Flyway.NAME, DbType.MYSQL.hibernateReactiveTestResourcesModuleName, MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()),
                new TestScenario(MariaDB.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT, 'mariadb', Flyway.NAME, DbType.MARIADB.hibernateReactiveTestResourcesModuleName, MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()),
                new TestScenario(PostgreSQL.NAME, PostgreSQL.VERTX_PG_CLIENT, 'postgresql', Flyway.NAME, DbType.POSTGRESQL.hibernateReactiveTestResourcesModuleName, PostgreSQL.DEPENDENCY_POSTGRESQL.build()),
                new TestScenario(Oracle.NAME, Oracle.VERTX_ORACLE_CLIENT, 'oracle-xe', Flyway.NAME, DbType.ORACLEFREE.hibernateReactiveTestResourcesModuleName, Oracle.DEPENDENCY_OJDBC11.build()),
                new TestScenario(SQLServer.NAME, SQLServer.VERTX_MSSQL_CLIENT, 'mssqlserver', Flyway.NAME, DbType.SQLSERVER.hibernateReactiveTestResourcesModuleName, SQLServer.DEPENDENCY_MSSQL_JDBC.build())
        ])
    }

    void "test kotlin jpa plugin is present for gradle kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('id("org.jetbrains.kotlin.plugin.jpa")')
    }

    void "test dependencies are present for maven with #testScenario.db (#testScenario.client)"(TestScenario testScenario) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, testScenario.db])
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        containsDataProcessor(template)
        !containsDataHibernateJpa(template)
        containsDataHibernateReactive(template)
        !containsHikariDependency(template)
        containsVertXDriver(template, testScenario.client)
        !containsJdbcDriver(template, testScenario.driver)

        and: "postgres needs another dependency with vert.x"
        testScenario.db != PostgreSQL.NAME || verifier.hasDependency('com.ongres.scram', "client")

        and: 'test resources module is correct, and driver is added to the plugin dependencies'
        verifier.hasTestResourceDependency("micronaut-test-resources-$testScenario.testResourcesModule")
        verifier.hasTestResourceDependency(testScenario.driver.groupId, testScenario.driver.artifactId)
        and:
        DataJdbcSpec.assertTemplateDoesNotContainMicronautDataVersionProperty(template)

        where:
        testScenario << mavenTestScenarios()
    }

    List<TestScenario> mavenTestScenarios() {
        filter([
                new TestScenario(MySQL.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT, null, null, DbType.MYSQL.hibernateReactiveTestResourcesModuleName, MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()),
                new TestScenario(MariaDB.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT , null, null, DbType.MARIADB.hibernateReactiveTestResourcesModuleName, MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()),
                new TestScenario(PostgreSQL.NAME , PostgreSQL.VERTX_PG_CLIENT, null, null, DbType.POSTGRESQL.hibernateReactiveTestResourcesModuleName, PostgreSQL.DEPENDENCY_POSTGRESQL.build()),
                new TestScenario(Oracle.NAME, Oracle.VERTX_ORACLE_CLIENT, null, null, DbType.ORACLEFREE.hibernateReactiveTestResourcesModuleName, Oracle.DEPENDENCY_OJDBC11.build()),
                new TestScenario(SQLServer.NAME, SQLServer.VERTX_MSSQL_CLIENT, null, null, DbType.SQLSERVER.hibernateReactiveTestResourcesModuleName, SQLServer.DEPENDENCY_MSSQL_JDBC.build()),
        ])
    }

    void "test dependencies are present for maven with #testScenario.db (#testScenario.client) and #testScenario.migration"(TestScenario testScenario) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features([DataHibernateReactive.NAME, testScenario.db, testScenario.migration])
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        containsDataProcessor(template)
        !containsDataHibernateJpa(template)
        containsDataHibernateReactive(template)
        containsHikariDependency(template)
        containsVertXDriver(template, testScenario.client)
        containsMigrationLibrary(template, testScenario.migration)
        containsJdbcDriver(template, testScenario.driver)

        and: "postgres needs another dependency with vert.x"
        testScenario.db != PostgreSQL.NAME || verifier.hasDependency('com.ongres.scram', "client")

        and: 'test resources module is correct, and driver is not added to the plugin dependencies'
        verifier.hasTestResourceDependency("micronaut-test-resources-$testScenario.testResourcesModule")
        !verifier.hasTestResourceDependencyWithGroupId(testScenario.driver.groupId)
        and:
        DataJdbcSpec.assertTemplateDoesNotContainMicronautDataVersionProperty(template)

        where:
        testScenario << mavenMigrationTestScenarios()
    }

    List<TestScenario> mavenMigrationTestScenarios() {
        filter([
                new TestScenario(MySQL.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT,null, Liquibase.NAME, DbType.MYSQL.hibernateReactiveTestResourcesModuleName, MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()),
                new TestScenario(MariaDB.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT,null, Liquibase.NAME, DbType.MARIADB.hibernateReactiveTestResourcesModuleName, MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()),
                new TestScenario(PostgreSQL.NAME, PostgreSQL.VERTX_PG_CLIENT,null, Liquibase.NAME, DbType.POSTGRESQL.hibernateReactiveTestResourcesModuleName, PostgreSQL.DEPENDENCY_POSTGRESQL.build()),
                new TestScenario(Oracle.NAME, Oracle.VERTX_ORACLE_CLIENT,null, Liquibase.NAME, DbType.ORACLEFREE.hibernateReactiveTestResourcesModuleName, Oracle.DEPENDENCY_OJDBC11.build()),
                new TestScenario(SQLServer.NAME, SQLServer.VERTX_MSSQL_CLIENT,null, Liquibase.NAME, DbType.SQLSERVER.hibernateReactiveTestResourcesModuleName, SQLServer.DEPENDENCY_MSSQL_JDBC.build()),
                new TestScenario(MySQL.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT , null, Flyway.NAME, DbType.MYSQL.hibernateReactiveTestResourcesModuleName, MySQL.DEPENDENCY_MYSQL_CONNECTOR_JAVA.build()),
                new TestScenario(MariaDB.NAME, MySQLCompatibleFeature.VERTX_MYSQL_CLIENT , null, Flyway.NAME, DbType.MARIADB.hibernateReactiveTestResourcesModuleName, MariaDB.DEPENDENCY_MARIADB_JAVA_CLIENT.build()),
                new TestScenario(PostgreSQL.NAME, PostgreSQL.VERTX_PG_CLIENT, null, Flyway.NAME, DbType.POSTGRESQL.hibernateReactiveTestResourcesModuleName, PostgreSQL.DEPENDENCY_POSTGRESQL.build()),
                new TestScenario(Oracle.NAME, Oracle.VERTX_ORACLE_CLIENT, null, Flyway.NAME, DbType.ORACLEFREE.hibernateReactiveTestResourcesModuleName, Oracle.DEPENDENCY_OJDBC11.build()),
                new TestScenario(SQLServer.NAME, SQLServer.VERTX_MSSQL_CLIENT, null, Flyway.NAME, DbType.SQLSERVER.hibernateReactiveTestResourcesModuleName, SQLServer.DEPENDENCY_MSSQL_JDBC.build()),
        ])
    }

    void "test kotlin jpa plugin is present for maven kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .language(Language.KOTLIN)
                .render()
        def xml = new XmlSlurper().parseText(template)
        def kotlinMavenPlugin = xml.build.plugins.plugin.find { it.artifactId.text() == 'kotlin-maven-plugin' }

        then:
        kotlinMavenPlugin.configuration.compilerPlugins.plugin*.text() == ['jpa', 'all-open']

        and: 'jpa plugin requires kotlin-maven-noarg dependency'
        with(kotlinMavenPlugin.dependencies.dependency) {
            it.groupId.every { it.text() == 'org.jetbrains.kotlin' }
            it.artifactId*.text() == ['kotlin-maven-allopen', 'kotlin-maven-noarg']
            it.version.every { it.text() == '${kotlinVersion}' }
        }
    }

    void "test config for #testScenario.db"(DbConfigTestScenario testScenario) {
        when:
        GeneratorContext ctx = buildGeneratorContext([DataHibernateReactive.NAME, testScenario.db])

        then:
        !ctx.configuration.containsKey("datasources.default.url")
        !ctx.configuration.containsKey("datasources.default.username")
        !ctx.configuration.containsKey("datasources.default.password")
        !ctx.configuration.containsKey("datasources.default.dialect")
        ctx.configuration."jpa.default.reactive" == true
        ctx.configuration."jpa.default.properties.hibernate.connection.db-type" == testScenario.dbType
        !ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.url")
        !ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.username")
        !ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.password")

        where:
        testScenario << dbConfigScenarios()
    }

    List<DbConfigTestScenario> dbConfigScenarios() {
        filterDbConfigTestScenario([
                new DbConfigTestScenario(MySQL.NAME, 'mysql'),
                new DbConfigTestScenario(MariaDB.NAME, 'mariadb'),
                new DbConfigTestScenario(PostgreSQL.NAME, 'postgres'),
                new DbConfigTestScenario(Oracle.NAME, 'oracle'),
                new DbConfigTestScenario(SQLServer.NAME,  'mssql'),
        ])
    }

    void "test config for #testScenario.db with #testScenario.migration"(DbConfigTestScenario testScenario) {
        when:
        GeneratorContext ctx = buildGeneratorContext([DataHibernateReactive.NAME, testScenario.db, testScenario.migration])

        then:
        !ctx.configuration.containsKey("datasources.default.url")
        !ctx.configuration.containsKey("datasources.default.username")
        !ctx.configuration.containsKey("datasources.default.password")
        !ctx.configuration.containsKey("datasources.default.dialect")
        ctx.configuration."datasources.default.db-type" == testScenario.dbType
        ctx.configuration."jpa.default.reactive" == true
        ctx.configuration."jpa.default.properties.hibernate.connection.db-type" == testScenario.dbType
        !ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.url")
        !ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.username")
        !ctx.configuration.containsKey("jpa.default.properties.hibernate.connection.password")

        where:
        testScenario << dbConfigWithMigrationScenarios()
    }

    List<DbConfigTestScenario> dbConfigWithMigrationScenarios() {
        filterDbConfigTestScenario([
                new DbConfigTestScenario(MySQL.NAME      , 'mysql'    , Flyway.NAME),
                new DbConfigTestScenario(MariaDB.NAME    , 'mariadb'  , Flyway.NAME),
                new DbConfigTestScenario(PostgreSQL.NAME , 'postgres' , Flyway.NAME),
                new DbConfigTestScenario(Oracle.NAME     , 'oracle'   , Flyway.NAME),
                new DbConfigTestScenario(SQLServer.NAME  , 'mssql'    , Flyway.NAME),
                new DbConfigTestScenario(MySQL.NAME      , 'mysql'    , Liquibase.NAME),
                new DbConfigTestScenario(MariaDB.NAME    , 'mariadb'  , Liquibase.NAME),
                new DbConfigTestScenario(PostgreSQL.NAME , 'postgres' , Liquibase.NAME),
                new DbConfigTestScenario(Oracle.NAME     , 'oracle'   , Liquibase.NAME),
                new DbConfigTestScenario(SQLServer.NAME  , 'mssql'    , Liquibase.NAME),
        ])
    }


        @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/686")
    void 'test data-processor dependency is in provided scope for Groovy and Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([DataHibernateReactive.NAME, MySQL.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency(GROUP_ID_MICRONAUT_DATA, ARTIFACT_ID_MICRONAUT_DATA_PROCESSOR_ARTIFACT, Scope.COMPILE_ONLY)
    }

    @Canonical
    class DbConfigTestScenario {
        String db
        String dbType
        String migration
    }

    @Canonical
    class TestScenario {
        String db
        String client
        String container
        String migration
        String testResourcesModule
        Dependency driver
    }

    private List<DbConfigTestScenario> filterDbConfigTestScenario(List<DbConfigTestScenario> scenarios) {
        scenarios.stream()
                .filter(ts -> ts.db != Oracle.NAME || Oracle.COMPATIBLE_WITH_HIBERNATE_REACTIVE)
                .toList()
    }

    private List<TestScenario> filter(List<TestScenario> scenarios) {
        scenarios.stream()
                .filter(ts -> ts.db != Oracle.NAME || Oracle.COMPATIBLE_WITH_HIBERNATE_REACTIVE)
                .toList()
    }
}
