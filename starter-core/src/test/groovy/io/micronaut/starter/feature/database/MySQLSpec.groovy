package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class MySQLSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle mysql feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['mysql'], language), false).render().toString()

        then:
        template.contains('runtimeOnly("mysql:mysql-connector-java")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven mysql feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['mysql'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
