package io.micronaut.starter.feature.migration

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
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


    void "test dependency added for AOP feature"(BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = verifier(buildTool, ['flyway'])

        then:
        verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    private BuildTestVerifier verifier(BuildTool buildTool, List<String> features) {
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .render()
        BuildTestUtil.verifier(buildTool, template)
    }

    void "test the flyway-mysql dependency is added to the gradle build"(BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = verifier(buildTool, ['flyway', 'mysql'])

        then:
        verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE)
        verifier.hasDependency("org.flywaydb", "flyway-mysql", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }

    void "test the flyway-mysql dependency is added to the gradle build when mariadb is selected"(BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = verifier(buildTool, ['flyway', 'mariadb'])

        then:
        verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE)
        verifier.hasDependency("org.flywaydb", "flyway-mysql", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }

    void "test the flyway-sqlserver dependency is added to the gradle build when sqlserver is selected"(BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = verifier(buildTool, ['flyway', 'sqlserver'])

        then:
        verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE)
        verifier.hasDependency("org.flywaydb", "flyway-sqlserver", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }

    void "test the flyway-database-oracle dependency is added to the gradle build when oracle-cloud-atp is selected"(BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = verifier(buildTool, ['flyway', 'oracle-cloud-atp'])

        then:
        verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE)
        verifier.hasDependency("org.flywaydb", "flyway-database-oracle", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }

    void "test the flyway-database-oracle dependency is added to the gradle build when oracle is selected"(BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = verifier(buildTool, ['flyway', 'oracle'])

        then:
        verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE)
        verifier.hasDependency("org.flywaydb", "flyway-database-oracle", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }
}
