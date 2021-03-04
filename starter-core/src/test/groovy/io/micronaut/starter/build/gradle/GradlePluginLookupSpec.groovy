package io.micronaut.starter.build.gradle

import spock.lang.Specification

class GradlePluginLookupSpec extends Specification {

    void "GradlePluginLookup implements equals and hash code"() {
        expect:
        new GradlePluginLookup("io.micronaut.application", "micronaut-gradle-plugin") ==
                new GradlePluginLookup("io.micronaut.application", "micronaut-gradle-plugin")
    }
}
