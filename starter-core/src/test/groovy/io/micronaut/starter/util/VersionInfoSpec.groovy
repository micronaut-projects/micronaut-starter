package io.micronaut.starter.util

import spock.lang.Specification

class VersionInfoSpec extends Specification {

    void "test get dependency version"() {
        given:
        def version = VersionInfo.getDependencyVersion("micronaut.data")

        expect:
        version.key == 'micronaut.data.version'
        version.value
    }
}
