package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.build.dependencies.Dependency

abstract class BaseHibernateReactiveSpec extends ApplicationContextSpec {

    boolean containsDataProcessor(String template) {
        template.contains('''\
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
''')
    }

    boolean containsDataHibernateJpa(String template) {
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
''')
    }

    boolean containsDataHibernateReactive(String template) {
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-reactive</artifactId>
      <scope>compile</scope>
    </dependency>
''')
    }

    boolean containsJdbcDriver(String template, Dependency migrationDriver) {
        template.contains("""\
    <dependency>
      <groupId>$migrationDriver.groupId</groupId>
      <artifactId>$migrationDriver.artifactId</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
    }

    boolean containsMigrationLibrary(String template, String migration) {
        template.contains("""\
    <dependency>
      <groupId>io.micronaut.$migration</groupId>
      <artifactId>micronaut-$migration</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    boolean containsVertXDriver(String template, String client) {
        template.contains("""\
    <dependency>
      <groupId>$HibernateReactiveFeature.IO_VERTX_DEPENDENCY_GROUP</groupId>
      <artifactId>$client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    boolean contansSqlHibernateReactive(String template) {
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-hibernate-reactive</artifactId>
      <scope>compile</scope>
    </dependency>
''')
    }

    boolean containsHikariDependency(String template) {
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
''')
    }
}
