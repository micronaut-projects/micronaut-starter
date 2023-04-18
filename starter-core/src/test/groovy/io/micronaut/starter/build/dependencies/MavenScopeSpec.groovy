package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.maven.MavenScope
import spock.lang.Specification

class MavenScopeSpec extends Specification {

    void "MavenScope::toString() returns the scope"() {
        expect:
        'runtime' == MavenScope.RUNTIME.toString()
    }

    void "#source #phases should return #scope"() {
        expect:
        scope == MavenScope.of(new Scope(source, phases)).get()

        where:
        source      | phases                                               || scope
        Source.MAIN | [Phase.DEVELOPMENT]                                  || MavenScope.PROVIDED
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION]                   || MavenScope.COMPILE
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION, Phase.PUBLIC_API] || MavenScope.COMPILE
        Source.MAIN | [Phase.RUNTIME]                                      || MavenScope.RUNTIME
        Source.MAIN | [Phase.COMPILATION]                                  || MavenScope.PROVIDED
        Source.TEST | [Phase.RUNTIME]                                      || MavenScope.TEST
        Source.TEST | [Phase.COMPILATION]                                  || MavenScope.TEST
        Source.TEST | [Phase.RUNTIME, Phase.COMPILATION]                   || MavenScope.TEST
    }
}
