package io.micronaut.starter.build.dependencies

import spock.lang.Specification
import spock.lang.Unroll

class MavenDependencyAdapterSpec extends Specification {

    @Unroll("#description")
    void "it is possible to adapt from source and phases to Maven scopes"(Source source,
                                                                          List<Phase> phases,
                                                                          MavenScope scope,
                                                                          String description) {
        scope.toString() == new MavenDependencyAdapter(new ScopedDependency() {
            Scope getScope() {
                new Scope(source, phases)
            }

            @Override
            String getGroupId() {
                "xxx.yyyy"
            }

            @Override
            String getArtifactId() {
                'zzzz'
            }
        }).scope

        where:
        source      | phases                              || scope
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION]  || MavenScope.COMPILE
        Source.MAIN | [Phase.RUNTIME]                     || MavenScope.RUNTIME
        Source.TEST | [Phase.RUNTIME]                     || MavenScope.TEST
        Source.TEST | [Phase.COMPILATION]                 || MavenScope.TEST
        Source.TEST | [Phase.RUNTIME, Phase.COMPILATION]  || MavenScope.TEST
        description = "$source ${phases.join(",")} should return ${scope.toString()}"
    }
}
