package io.micronaut.starter.build.dependencies

import spock.lang.Specification

class ScopedDependencySpec extends Specification {

    void "ScopedDependency implements equals and hash code"() {
        expect:
        new ScopedDependency(new Scope(Source.MAIN, [Phase.RUNTIME]), "io.zipkin.brave", "brave-instrumentation-http") ==
                new ScopedDependency(new Scope(Source.MAIN, [Phase.RUNTIME]), "io.zipkin.brave", "brave-instrumentation-http")
    }
}
