package io.micronaut.starter.feature.database.r2dbc

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import spock.lang.Unroll

class R2dbcSpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void "test gradle r2dbc feature #driver"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["r2dbc", driver]), false).render().toString()
        driver = getDriverName(driver)

        then:
        template.contains("implementation(\"io.micronaut.r2dbc:micronaut-r2dbc-core\")")
        template.contains(":r2dbc-$driver")

        where:
        driver << beanContext.getBeansOfType(DatabaseDriverFeature)*.name - "oracle"
    }

    @Unroll
    void "test maven r2dbc feature #driver"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["r2dbc", driver]), []).render().toString()
        driver = getDriverName(driver)

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.r2dbc</groupId>
      <artifactId>micronaut-r2dbc-core</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("<artifactId>r2dbc-$driver</artifactId>")

        where:
        driver << beanContext.getBeansOfType(DatabaseDriverFeature)*.name - "oracle"
    }

    def getDriverName(name) {
        if (name == 'sqlserver') {
            return "mssql"
        }
        if (name == 'postgres') {
            return "postgresql"
        }
        return name
    }
}
