package io.micronaut.starter.feature.testresources

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.database.r2dbc.DataR2dbc
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class TestResourcesR2dbcSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Unroll
    void "test r2dbc with #feature and #dbtype and test-resources"() {
        when:
        Options options = new Options(null, null, BuildTool.GRADLE)
        GeneratorContext ctx = buildGeneratorContext([DataR2dbc.NAME, feature, 'test-resources'], options)

        then:
        ctx.configuration.containsKey("r2dbc.datasources.default.db-type")
        ctx.configuration.get("r2dbc.datasources.default.db-type") == dbtype as String

        where:
        feature     | dbtype
        'postgres'  | DbType.POSTGRESQL
        'mysql'     | DbType.MYSQL
        'mariadb'   | DbType.MARIADB
        'sqlserver' | DbType.SQLSERVER
        'oracle'    | DbType.ORACLEXE
    }
}
