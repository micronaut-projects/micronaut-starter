package io.micronaut.starter.feature.cassandra

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JooqSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle cassandra feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jooq'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.configuration:micronaut-jooq")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven cassandra feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jooq'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-jooq</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
