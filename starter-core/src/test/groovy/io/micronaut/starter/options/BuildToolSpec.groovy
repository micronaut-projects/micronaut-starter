package io.micronaut.starter.options

import spock.lang.Specification

class BuildToolSpec extends Specification {

    void "valueGradle return Groovy and Kotlin DSL"() {
        expect:
        BuildTool.valuesGradle().stream().allMatch { it.isGradle() }
        [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN] =~ BuildTool.valuesGradle()
    }
}
