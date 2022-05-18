package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.maven.MavenScope
import spock.lang.Specification
import spock.lang.Unroll

class MavenScopeSpec extends Specification {

    void "MavenScope::toString() returns the scope"() {
        expect:
        'runtime' == MavenScope.RUNTIME.toString()
    }

    @Unroll("#description")
    void "it is possible to adapt from source and phases to Maven scopes"(Source source,
                                                                          List<Phase> phases,
                                                                          MavenScope scope,
                                                                          String description) {
        expect:
        scope == MavenScope.of(new Scope(source, phases)).get()

        where:
        source      | phases                              || scope
        Source.MAIN | [Phase.DEVELOPMENT]                 || MavenScope.PROVIDED
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION]  || MavenScope.COMPILE
        Source.MAIN | [Phase.RUNTIME]                     || MavenScope.RUNTIME
        Source.MAIN | [Phase.COMPILATION]                 || MavenScope.PROVIDED
        Source.TEST | [Phase.RUNTIME]                     || MavenScope.TEST
        Source.TEST | [Phase.COMPILATION]                 || MavenScope.TEST
        Source.TEST | [Phase.RUNTIME, Phase.COMPILATION]  || MavenScope.TEST
        description = "$source ${phases.join(",")} should return ${scope.toString()}"
    }
}
