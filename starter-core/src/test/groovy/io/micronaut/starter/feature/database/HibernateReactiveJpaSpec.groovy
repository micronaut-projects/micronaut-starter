package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language

class HibernateReactiveJpaSpec extends ApplicationContextSpec {

    void "test hibernate reactive jpa requires db"() {
        when:
        getFeatures([HibernateReactiveJpa.NAME])

        then:
        IllegalArgumentException exception = thrown()

        and:
        exception.message == "Hibernate Reactive requires $MariaDB.NAME, $MySQL.NAME, $Oracle.NAME, $PostgreSQL.NAME, or $SQLServer.NAME"
    }

    void "test hibernate reactive jpa features for #db"() {
        when:
        Features features = getFeatures([HibernateReactiveJpa.NAME, db])

        then:
        !features.contains("data")
        features.contains(db)
        features.contains("reactor")
        !features.contains("jdbc-hikari")
        features.contains(HibernateReactiveJpa.NAME)

        where:
        db << [MySQL.NAME, MariaDB.NAME, PostgreSQL.NAME, Oracle.NAME, SQLServer.NAME]
    }

    void "data hibernate reactive requires java 11"() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_8)
                .features([HibernateReactiveJpa.NAME, MySQL.NAME])
                .render()

        then:
        def ex = thrown(IllegalArgumentException)
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
                .features([HibernateReactiveJpa.NAME, db])
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        !template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        !template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-reactive")')
        template.contains('implementation("io.micronaut.sql:micronaut-hibernate-reactive")')
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

    void "test kotlin jpa plugin is present for gradle kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([HibernateReactiveJpa.NAME, MySQL.NAME])
                .jdkVersion(JdkVersion.JDK_11)
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('id("org.jetbrains.kotlin.plugin.jpa")')
    }

    void "test dependencies are present for maven with #db (#client)"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([HibernateReactiveJpa.NAME, db])
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        //src/main
        !template.contains('''\
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
''')
        !template.contains('''\
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        !template.contains('''\
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-reactive</artifactId>
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
      <groupId>$HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP</groupId>
      <artifactId>$client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        !semanticVersionOptional.isPresent()

        where:
        db              | client
        MySQL.NAME      | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT
        MariaDB.NAME    | MySQLCompatibleFeature.VERTX_MYSQL_CLIENT
        PostgreSQL.NAME | PostgreSQL.VERTX_PG_CLIENT
        Oracle.NAME     | Oracle.VERTX_ORACLE_CLIENT
        SQLServer.NAME  | SQLServer.VERTX_MSSQL_CLIENT
    }

    void "test kotlin jpa plugin is present for maven kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([HibernateReactiveJpa.NAME, MySQL.NAME])
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
        GeneratorContext ctx = buildGeneratorContext([HibernateReactiveJpa.NAME, db])

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
}
