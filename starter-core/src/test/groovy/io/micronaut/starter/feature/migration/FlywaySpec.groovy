package io.micronaut.starter.feature.migration


import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

class FlywaySpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature flyway contains links to micronaut docs'() {
        when:
        def output = generate(['flyway'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://flywaydb.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-flyway/latest/guide/index.html")
    }

    void "test the dependency is added to the gradle build"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['flyway']), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.flyway:micronaut-flyway")')
    }

    void "test the dependency is added to the maven build"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['flyway']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.flyway</groupId>
      <artifactId>micronaut-flyway</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}