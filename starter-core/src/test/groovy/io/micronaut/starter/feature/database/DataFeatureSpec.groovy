package io.micronaut.starter.feature.database


import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class DataFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'Data Feature with migration has expected config'(List<String> features) {
        when:
        Map<String, String> output = generate(features)
        String appConfig = output["src/main/resources/application.yml"]

        then:
        appConfig
        appConfig.contains("dialect")
        !appConfig.contains("schema-generate")

        where:
        features                                  | _
        ['yaml', 'data-jdbc',  'flyway',    'mariadb']    | _
        ['yaml', 'data-jdbc',  'flyway',    'mysql']      | _
        ['yaml', 'data-jdbc',  'flyway',    'oracle']     | _
        ['yaml', 'data-jdbc',  'flyway',    'postgres']   | _
        ['yaml', 'data-jpa',   'flyway',    'mysql']      | _
        ['yaml', 'data-jpa',   'flyway',    'postgres']   | _
        ['yaml', 'data-r2dbc', 'flyway',    'mysql']      | _
        ['yaml', 'data-r2dbc', 'flyway',    'postgres']   | _
        ['yaml', 'data-r2dbc', 'flyway',    'sqlserver']  | _
        ['yaml', 'data-jdbc',  'liquibase', 'mariadb']    | _
        ['yaml', 'data-jdbc',  'liquibase', 'mysql']      | _
        ['yaml', 'data-jdbc',  'liquibase', 'oracle']     | _
        ['yaml', 'data-jdbc',  'liquibase', 'postgres']   | _
        ['yaml', 'data-jpa',   'liquibase', 'mysql']      | _
        ['yaml', 'data-jpa',   'liquibase', 'postgres']   | _
        ['yaml', 'data-r2dbc', 'liquibase', 'mysql']      | _
        ['yaml', 'data-r2dbc', 'liquibase', 'postgres']   | _
        ['yaml', 'data-r2dbc', 'liquibase', 'sqlserver']  | _
    }
}
