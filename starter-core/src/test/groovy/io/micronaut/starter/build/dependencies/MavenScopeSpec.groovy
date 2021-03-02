package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.maven.MavenScope
import spock.lang.Specification

class MavenScopeSpec extends Specification {

    void "MavenScope::toString() returns the scope"() {
        expect:
        'runtime' == MavenScope.RUNTIME.toString()
    }
}
