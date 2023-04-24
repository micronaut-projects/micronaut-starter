package io.micronaut.starter.feature.migration

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class FlywaySpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature flyway contains links to micronaut docs'() {
        when:
        def output = generate(['flyway'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://flywaydb.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-flyway/latest/guide/index.html")
    }

    void 'test feature flyway contains configuration'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'flyway'])
        String config = output["src/main/resources/application.yml"]

        then:
        config
        config.contains("""
flyway:
  datasources:
    default:
      enabled: true
""")
    }

    void "test the dependency is added to the gradle build"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['flyway'])
                .render()

        then:
        template.contains('implementation("io.micronaut.flyway:micronaut-flyway")')
    }

    void "test the dependency is added to the maven build"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['flyway'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.flyway</groupId>
      <artifactId>micronaut-flyway</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    void "test the flyway-mysql dependency is added to the gradle build"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['flyway', 'mysql'])
                .render()

        then:
        template.contains('runtimeOnly("org.flywaydb:flyway-mysql")')
    }

    void "test the flyway-mysql dependency is added to the gradle build when mariadb is selected"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['flyway', 'mariadb'])
                .render()

        then:
        template.contains('runtimeOnly("org.flywaydb:flyway-mysql")')
    }

    void "test the flyway-sqlserver dependency is added to the gradle build"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['flyway', 'sqlserver'])
                .render()

        then:
        template.contains('runtimeOnly("org.flywaydb:flyway-sqlserver")')
    }
}
