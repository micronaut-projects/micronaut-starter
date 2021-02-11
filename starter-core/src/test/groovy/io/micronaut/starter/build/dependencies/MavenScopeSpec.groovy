package io.micronaut.starter.build.dependencies

import spock.lang.Specification

class MavenScopeSpec extends Specification {

    void "MavenScope::toString() returns the scope"() {
        expect:
        'runtime' == MavenScope.RUNTIME.toString()
    }
}
