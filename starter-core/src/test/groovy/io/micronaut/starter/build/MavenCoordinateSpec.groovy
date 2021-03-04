package io.micronaut.starter.build

import io.micronaut.starter.build.dependencies.Phase
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.build.dependencies.ScopedDependency
import io.micronaut.starter.build.dependencies.Source
import spock.lang.Specification

class MavenCoordinateSpec extends Specification {
    void "MavenCoordinate implements equals and hash code"() {
        expect:
        new MavenCoordinate("io.zipkin.brave", "brave-instrumentation-http") ==
                new MavenCoordinate("io.zipkin.brave", "brave-instrumentation-http")
    }
}
