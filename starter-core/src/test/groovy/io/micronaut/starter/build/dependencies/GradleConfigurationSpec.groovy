package io.micronaut.starter.build.dependencies

import io.micronaut.starter.build.gradle.GradleConfiguration
import spock.lang.Specification
import spock.lang.Unroll

class GradleConfigurationSpec extends Specification {
    void "GradleConfiguration::toString() returns the gradle configuration"() {
        expect:
        'runtimeOnly' == GradleConfiguration.RUNTIME_ONLY.toString()
    }

    @Unroll("#description")
    void "it is possible to adapt from source and phases to Gradle configuration"(Source source,
                                                                                  List<Phase> phases, GradleConfiguration configuration,
                                                                                  String description) {
        configuration == GradleConfiguration.of(new Scope(source, phases)).get()

        where:
        source      | phases                              || configuration
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION]  || GradleConfiguration.IMPLEMENTATION
        Source.MAIN | [Phase.RUNTIME]                     || GradleConfiguration.RUNTIME_ONLY
        Source.TEST | [Phase.RUNTIME]                     || GradleConfiguration.TEST_RUNTIME_ONLY
        Source.MAIN | [Phase.COMPILATION]                 || GradleConfiguration.COMPILE_ONLY
        Source.TEST | [Phase.COMPILATION]                 || GradleConfiguration.TEST_COMPILE_ONLY
        Source.TEST | [Phase.RUNTIME, Phase.COMPILATION]  || GradleConfiguration.TEST_IMPLEMENTATION
        Source.MAIN | [Phase.ANNOTATION_PROCESSING]       || GradleConfiguration.ANNOTATION_PROCESSOR
        Source.TEST | [Phase.ANNOTATION_PROCESSING]       || GradleConfiguration.TEST_ANNOTATION_PROCESSOR
        description = "$source ${phases.join(",")} should return ${configuration.toString()}"
    }
}
