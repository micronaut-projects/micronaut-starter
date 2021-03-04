package io.micronaut.starter.build.dependencies

import spock.lang.Specification

class DependencyLookupSpec extends Specification {

    void "DependencyLookup implements equals and hash code"() {
        expect:
        new DependencyLookup(new Scope(Source.MAIN, [Phase.COMPILATION]), "micronaut-azure-function") ==
                new DependencyLookup(new Scope(Source.MAIN, [Phase.COMPILATION]), "micronaut-azure-function")
    }
}
