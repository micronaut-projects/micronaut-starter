package io.micronaut.starter.feature.database.r2dbc

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.feature.database.H2
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class R2dbcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "test gradle r2dbc feature #driver"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["r2dbc", driver])
                .render()
        driver = getDriverName(driver)

        then:
        template.contains("implementation(\"io.micronaut.r2dbc:micronaut-r2dbc-core\")")
        template.contains(":$driver")

        where:
        driver << (beanContext.getBeansOfType(DatabaseDriverFeature)*.name) - "oracle-cloud-atp"
    }

    void "test dependencies are present for gradle with #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)

        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([R2dbc.NAME, feature.name])
                .render()

        def jdbcDriver = renderDependency(feature.javaClientDependency.get().build())
        def r2dbcDriver = renderDependency(feature.r2DbcDependency.get().build())

        then: 'test-resources is applied for all but H2'
        template.contains('id("io.micronaut.test-resources") version') == isNotH2

        and: 'the data processor is not applied'
        !template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        !template.contains('implementation("io.micronaut.data:micronaut-data-r2dbc")')
        template.contains('implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")')

        and: 'the r2dbc driver is applied'
        template.contains($/runtimeOnly("$r2dbcDriver")/$)

        and: 'for test resources, the JDBC driver is applied to the test-resources service unless it is H2'
        template.contains($/testResourcesService("$jdbcDriver")/$) == isNotH2

        and: 'the jdbc driver is not applied'
        !template.contains($/runtimeOnly("$jdbcDriver")/$)

        where:
        db << [H2, PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
        isNotH2 = db != H2
    }

    @Unroll
    void "test maven r2dbc feature #driver"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["r2dbc", driver])
                .render()
        driver = getDriverName(driver)
        JdbcFeature jdbcFeature = beanContext.getBean(JdbcFeature)

        then:
        jdbcFeature.name == 'jdbc-hikari'
        template.contains("""
    <dependency>
      <groupId>io.micronaut.r2dbc</groupId>
      <artifactId>micronaut-r2dbc-core</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        !template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("<artifactId>$driver</artifactId>")

        where:
        driver << (beanContext.getBeansOfType(DatabaseDriverFeature)*.name) - "oracle-cloud-atp"
    }

    def getDriverName(name) {
        if (name == 'sqlserver') {
            return "r2dbc-mssql"
        }
        if (name == 'postgres') {
            return "r2dbc-postgresql"
        }
        if (name == 'oracle') {
            return "oracle-r2dbc"
        }
        return "r2dbc-$name"
    }
}
