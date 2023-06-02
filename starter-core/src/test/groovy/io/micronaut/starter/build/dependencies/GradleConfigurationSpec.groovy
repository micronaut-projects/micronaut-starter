package io.micronaut.starter.build.dependencies

import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.gradle.GradleConfiguration
import io.micronaut.starter.build.gradle.KotlinSymbolProcessingFeature
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Specification

class GradleConfigurationSpec extends Specification {

    void "GradleConfiguration::toString() returns the gradle configuration '#expected'"() {
        expect:
        expected == gradleConfiguration.toString()

        where:
        expected                  | gradleConfiguration
        'runtimeOnly'             | GradleConfiguration.RUNTIME_ONLY
        'developmentOnly'         | GradleConfiguration.DEVELOPMENT_ONLY
        'testImplementation'      | GradleConfiguration.TEST_IMPLEMENTATION
        'implementation'          | GradleConfiguration.IMPLEMENTATION
        'runtimeOnly'             | GradleConfiguration.RUNTIME_ONLY
        'compileOnly'             | GradleConfiguration.COMPILE_ONLY
        'testCompileOnly'         | GradleConfiguration.TEST_COMPILE_ONLY
        'kapt'                    | GradleConfiguration.KAPT
        'ksp'                     | GradleConfiguration.KSP
        'kspTest'                 | GradleConfiguration.TEST_KSP
        'kaptTest'                | GradleConfiguration.TEST_KAPT
        'rewrite'                 | GradleConfiguration.OPENREWRITE
        'annotationProcessor'     | GradleConfiguration.ANNOTATION_PROCESSOR
        'testAnnotationProcessor' | GradleConfiguration.TEST_ANNOTATION_PROCESSOR
        'api'                     | GradleConfiguration.API
    }

    void "#source #phases should return #configuration"() {
        expect:
        configuration == GradleConfiguration.of(new Scope(source, phases), Language.JAVA, TestFramework.JUNIT, null).get()

        where:
        source      | phases                                               || configuration
        Source.MAIN | [Phase.DEVELOPMENT]                                  || GradleConfiguration.DEVELOPMENT_ONLY
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION, Phase.PUBLIC_API] || GradleConfiguration.API
        Source.MAIN | [Phase.RUNTIME, Phase.COMPILATION]                   || GradleConfiguration.IMPLEMENTATION
        Source.MAIN | [Phase.RUNTIME]                                      || GradleConfiguration.RUNTIME_ONLY
        Source.TEST | [Phase.RUNTIME]                                      || GradleConfiguration.TEST_RUNTIME_ONLY
        Source.MAIN | [Phase.COMPILATION]                                  || GradleConfiguration.COMPILE_ONLY
        Source.TEST | [Phase.COMPILATION]                                  || GradleConfiguration.TEST_COMPILE_ONLY
        Source.TEST | [Phase.RUNTIME, Phase.COMPILATION]                   || GradleConfiguration.TEST_IMPLEMENTATION
        Source.MAIN | [Phase.ANNOTATION_PROCESSING]                        || GradleConfiguration.ANNOTATION_PROCESSOR
        Source.TEST | [Phase.ANNOTATION_PROCESSING]                        || GradleConfiguration.TEST_ANNOTATION_PROCESSOR
    }

    void "if ksp present #source #phases should return #configuration"(boolean ksp, Source source, List<Phase> phases, GradleConfiguration configuration) {
        given:
        def generatorContext = Stub(GeneratorContext) {
            isFeaturePresent(KotlinSymbolProcessingFeature.class) >> ksp
        }
        expect:
        configuration == GradleConfiguration.of(new Scope(source, phases), Language.KOTLIN, TestFramework.JUNIT, generatorContext).get()

        where:
        ksp   | source      | phases                        || configuration
        true  | Source.MAIN | [Phase.ANNOTATION_PROCESSING] || GradleConfiguration.KSP
        false | Source.MAIN | [Phase.ANNOTATION_PROCESSING] || GradleConfiguration.KAPT
    }
}
