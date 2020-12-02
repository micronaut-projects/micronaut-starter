package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SQLServerSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle sqlserver feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['sqlserver'], language), false).render().toString()

        then:
        template.contains('runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven sqlserver feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['sqlserver'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>mssql-jdbc</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
