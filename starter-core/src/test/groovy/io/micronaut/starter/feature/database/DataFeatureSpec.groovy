package io.micronaut.starter.feature.database


import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class DataFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'Data Feature with migration has expected config'(List<String> features) {
        when:
        def output = generate(features)
        def appConfig = output["src/main/resources/application.yml"]

        then:
        appConfig
        appConfig.contains("dialect")
        !appConfig.contains("schema-generate")

        where:
        features                                  | _
        ['data-jdbc',  'flyway',    'mariadb']    | _
        ['data-jdbc',  'flyway',    'mysql']      | _
        ['data-jdbc',  'flyway',    'oracle']     | _
        ['data-jdbc',  'flyway',    'postgres']   | _
        ['data-jpa',   'flyway',    'mysql']      | _
        ['data-jpa',   'flyway',    'postgres']   | _
        ['data-r2dbc', 'flyway',    'mysql']      | _
        ['data-r2dbc', 'flyway',    'postgres']   | _
        ['data-r2dbc', 'flyway',    'sqlserver']  | _
        ['data-jdbc',  'liquibase', 'mariadb']    | _
        ['data-jdbc',  'liquibase', 'mysql']      | _
        ['data-jdbc',  'liquibase', 'oracle']     | _
        ['data-jdbc',  'liquibase', 'postgres']   | _
        ['data-jpa',   'liquibase', 'mysql']      | _
        ['data-jpa',   'liquibase', 'postgres']   | _
        ['data-r2dbc', 'liquibase', 'mysql']      | _
        ['data-r2dbc', 'liquibase', 'postgres']   | _
        ['data-r2dbc', 'liquibase', 'sqlserver']  | _
    }
}
