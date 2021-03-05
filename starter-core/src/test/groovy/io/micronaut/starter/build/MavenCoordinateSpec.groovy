package io.micronaut.starter.build

import io.micronaut.starter.build.dependencies.Dependency
import spock.lang.Specification

class MavenCoordinateSpec extends Specification {
    void "MavenCoordinate implements equals and hash code"() {
        expect:
        Dependency.builder().groupId("io.zipkin.brave").artifactId("brave-instrumentation-http").buildCoordinate() ==
                Dependency.builder().groupId("io.zipkin.brave").artifactId("brave-instrumentation-http").buildCoordinate()
    }
}
