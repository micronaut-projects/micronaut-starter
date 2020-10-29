package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class PostgreSQLSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle postgres feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['postgres'], language), false).render().toString()

        then:
        template.contains('runtimeOnly("org.postgresql:postgresql")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven postgres feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['postgres'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
