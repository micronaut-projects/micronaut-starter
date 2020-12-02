package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JooqSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jooq contains links to micronaut docs'() {
        when:
        def output = generate(['jooq'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq")
    }

    @Unroll
    void 'test gradle jooq feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jooq'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jooq")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jooq feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jooq'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jooq</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
