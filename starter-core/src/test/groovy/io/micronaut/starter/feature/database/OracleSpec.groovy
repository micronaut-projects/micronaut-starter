package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class OracleSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle oracle feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['oracle'], language), false).render().toString()

        then:
        template.contains('runtimeOnly("com.oracle.database.jdbc:ojdbc8")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven oracle feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['oracle'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>com.oracle.database.jdbc</groupId>
      <artifactId>ojdbc8</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
