package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

import java.util.stream.Collectors

class OracleSpec extends BeanContextSpec implements CommandOutputFixture {

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

    @Unroll
    void "test exception for oracle and #r2dbcFeature features"() {
        when:
        generate(['oracle',r2dbcFeature])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("R2DBC is not yet supported by Oracle")

        where:
        r2dbcFeature << beanContext.streamOfType(R2dbcFeature)
                .map { f -> f.name }
                .collect(Collectors.toList())
    }
}
