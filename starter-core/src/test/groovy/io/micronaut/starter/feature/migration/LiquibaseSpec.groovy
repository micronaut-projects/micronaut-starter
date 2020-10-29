package io.micronaut.starter.feature.migration

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

class LiquibaseSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature liquibase contains links to micronaut docs'() {
        when:
        def output = generate(['liquibase'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.liquibase.org")
        readme.contains("https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html")
    }

    void "test the dependency is added to the gradle build"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['liquibase']), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.liquibase:micronaut-liquibase")')
    }

    void "test the dependency is added to the maven build"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['liquibase']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.liquibase</groupId>
      <artifactId>micronaut-liquibase</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
