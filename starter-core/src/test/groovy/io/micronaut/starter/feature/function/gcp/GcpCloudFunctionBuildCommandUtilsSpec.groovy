package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.options.BuildTool
import spock.lang.Specification
import spock.lang.Unroll

class GcpCloudFunctionBuildCommandUtilsSpec extends Specification {
    @Unroll("For #buildTool build command is #expected")
    void "gcp build command"(String expected, BuildTool buildTool) {
        expect:
        expected == GcpCloudFunctionBuildCommandUtils.getBuildCommand(buildTool)

        where:
        expected | buildTool
        'mvnw clean package' | BuildTool.MAVEN
        'gradlew shadowJar' | BuildTool.GRADLE
        'gradlew shadowJar' | BuildTool.GRADLE_KOTLIN
    }

    @Unroll("For #buildTool run command is #expected")
    void "gcp run command"(String expected, BuildTool buildTool) {
        expect:
        expected == GcpCloudFunctionBuildCommandUtils.getRunCommand(buildTool)

        where:
        expected | buildTool
        'mvnw function:run' | BuildTool.MAVEN
        'gradlew runFunction' | BuildTool.GRADLE
        'gradlew runFunction' | BuildTool.GRADLE_KOTLIN
    }
}