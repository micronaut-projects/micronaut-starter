package io.micronaut.starter

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class ApplicationContextSpec extends Specification implements ProjectFixture, ContextFixture {

    Map<String, Object> getConfiguration() {
        [:]
    }

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run(configuration)
}
