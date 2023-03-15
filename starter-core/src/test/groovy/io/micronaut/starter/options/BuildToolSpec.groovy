package io.micronaut.starter.options

import spock.lang.Specification

class BuildToolSpec extends Specification {

    void "valueGradle return Groovy and Kotlin DSL"() {
        BuildTool.valuesGradle().stream().allMatch { it.isGradle() }
        BuildTool.valuesGradle().stream().anyMatch { BuildTool.GRADLE_KOTLIN }
        BuildTool.valuesGradle().stream().anyMatch { BuildTool.GRADLE }
        [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN].size() == BuildTool.valuesGradle().size()
    }

}
