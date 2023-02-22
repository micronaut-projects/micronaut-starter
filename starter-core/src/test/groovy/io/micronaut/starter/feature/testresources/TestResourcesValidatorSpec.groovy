package io.micronaut.starter.feature.testresources

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.feature.database.r2dbc.DataR2dbc
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class TestResourcesValidatorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "test r2dbc with illegal feature and test-resources"() {
        when:
        generate(["mongo-sync", 'r2dbc', 'test-resources'])

        then:
        def e = thrown IllegalArgumentException
        e.message == "test-resources with r2dbc requires one of mariadb, mysql, oracle, postgres or sqlserver"
    }

    @Unroll
    void "test r2dbc with no DatabaseDriverFeature and test-resources"() {
        when:
        generate(['r2dbc', 'test-resources'])

        then:
        def e = thrown IllegalArgumentException
        e.message == "test-resources with r2dbc requires one of mariadb, mysql, oracle, postgres or sqlserver"
    }

    @Unroll
    void "test r2dbc with #feature and test-resources"() {
        when:
        Options options = new Options(null, null, BuildTool.GRADLE)
        buildGeneratorContext([DataR2dbc.NAME, feature, 'test-resources'], options)

        then:
        notThrown Exception

        where:
        feature << ['postgres','mysql','mariadb','sqlserver','oracle']
    }

}
