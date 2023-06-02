package io.micronaut.starter.feature.migration

import io.micronaut.starter.BeanContextSpec

class MigrationSpec extends BeanContextSpec {

    void "test there can only be one migration feature"() {
        when:
        getFeatures(["flyway", "liquibase"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}
