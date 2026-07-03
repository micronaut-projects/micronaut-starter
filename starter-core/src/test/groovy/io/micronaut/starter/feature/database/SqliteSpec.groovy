package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_SQL
import static io.micronaut.starter.feature.database.Sqlite.MICRONAUT_SQLITE_ARTIFACT
import static io.micronaut.starter.feature.database.Sqlite.NAME
import static io.micronaut.starter.feature.database.jdbc.Hikari.MICRONAUT_JDBC_HIKARI_ARTIFACT

class SqliteSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Sqlite sqlite = beanContext.getBean(Sqlite)

    void 'test readme.md with feature sqlite contains links to documentation'() {
        when:
        Map<String, String> output = generate([NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/#jdbc-sqlite")
        readme.contains("https://www.sqlite.org/")
    }

    void "test sqlite belongs to Database category"() {
        expect:
        Category.DATABASE == sqlite.category
    }

    void "test sqlite supports application type #appType"(ApplicationType appType) {
        expect:
        sqlite.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "test sqlite metadata"() {
        expect:
        sqlite.embedded()
        sqlite.jdbcUrl == "jdbc:sqlite:file:%s?mode=memory&cache=shared&foreign_keys=on&busy_timeout=5000"
        sqlite.r2dbcUrl == null
        sqlite.driverClass == "org.sqlite.JDBC"
        sqlite.defaultUser == null
        sqlite.defaultPassword == null
        sqlite.dataDialect == "SQLITE"
    }

    void "test dependencies are present for buildTool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency(GROUP_ID_MICRONAUT_SQL, MICRONAUT_SQLITE_ARTIFACT, Scope.COMPILE)
        verifier.hasDependency(GROUP_ID_MICRONAUT_SQL, MICRONAUT_JDBC_HIKARI_ARTIFACT, Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void "test sqlite feature configuration"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([NAME])
        Map datasourceConfig = ctx.configuration.get("datasources").get("default") as Map

        then:
        datasourceConfig.get("driver-class-name") == "org.sqlite.JDBC"
        !datasourceConfig.containsKey("url")
        !datasourceConfig.containsKey("username")
        !datasourceConfig.containsKey("password")
    }
}
