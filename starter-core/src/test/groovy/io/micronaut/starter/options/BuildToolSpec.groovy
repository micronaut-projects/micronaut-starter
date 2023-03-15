package io.micronaut.starter.options

import spock.lang.Specification

class BuildToolSpec extends Specification {

    void "valueGradle return Groovy and Kotlin DSL"() {
        expect:
        BuildTool.valuesGradle().stream().allMatch { it.isGradle() }
        BuildTool.valuesGradle().stream().anyMatch { it == BuildTool.GRADLE_KOTLIN }
        BuildTool.valuesGradle().stream().anyMatch { it == BuildTool.GRADLE }
        [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN].size() == BuildTool.valuesGradle().size()
    }
}
