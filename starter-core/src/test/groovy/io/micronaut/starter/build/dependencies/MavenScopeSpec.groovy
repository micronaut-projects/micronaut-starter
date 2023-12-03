package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.maven.MavenScope
import io.micronaut.starter.options.Language
import spock.lang.Specification
import spock.lang.Unroll

class MavenScopeSpec extends Specification {

    void "MavenScope::toString() returns the scope"() {
        expect:
        'runtime' == MavenScope.RUNTIME.toString()
    }

    @Unroll("#source #phases should return #scope")
    void "verify MavenScope::of"(Source source, List<Phase> phases, Language language, MavenScope scope) {
        expect:
        scope == MavenScope.of(new Scope(source, phases, 1), language).get()

        where:
        source      | phases                                               | language        || scope
        Source.MAIN | [Phase.DEVELOPMENT]                                  | Language.JAVA   || MavenScope.PROVIDED
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION]                   | Language.JAVA   || MavenScope.COMPILE
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION, Phase.PUBLIC_API] | Language.JAVA   || MavenScope.COMPILE
        Source.MAIN | [Phase.RUNTIME]                                      | Language.JAVA   || MavenScope.RUNTIME
        Source.MAIN | [Phase.COMPILATION]                                  | Language.JAVA   || MavenScope.PROVIDED
        Source.TEST | [Phase.RUNTIME]                                      | Language.JAVA   || MavenScope.TEST
        Source.TEST | [Phase.COMPILATION]                                  | Language.JAVA   || MavenScope.TEST
        Source.TEST | [Phase.RUNTIME, Phase.COMPILATION]                   | Language.JAVA   || MavenScope.TEST
        Source.MAIN | [Phase.ANNOTATION_PROCESSING]                        | Language.GROOVY || MavenScope.PROVIDED
    }
}
