package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture

class TestcontainersSpec extends BeanContextSpec implements CommandOutputFixture {



    void "test micronaut data jdbc with test containers"() {
        when:
        generate(ApplicationType.DEFAULT, ["data-jdbc", "testcontainers", driverFeature])


    }
}
