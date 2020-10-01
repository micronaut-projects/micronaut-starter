package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class MariaDBSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle mariadb feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['mariadb'], language), false).render().toString()

        then:
        template.contains('runtimeOnly("org.mariadb.jdbc:mariadb-java-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven mariadb feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['mariadb'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.mariadb.jdbc</groupId>
      <artifactId>mariadb-java-client</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
