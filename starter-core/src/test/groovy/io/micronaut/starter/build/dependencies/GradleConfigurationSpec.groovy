package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.gradle.GradleConfiguration
import spock.lang.Specification

class GradleConfigurationSpec extends Specification {
    void "GradleConfiguration::toString() returns the gradle configuration"() {
        expect:
        'runtimeOnly' == GradleConfiguration.RUNTIME_ONLY.toString()
    }
}
