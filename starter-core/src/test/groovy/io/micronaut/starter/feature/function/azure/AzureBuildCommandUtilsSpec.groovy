package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.options.BuildTool
import spock.lang.Specification

class AzureBuildCommandUtilsSpec extends Specification {

    void "for #buildTool build command is #expected"(String expected, BuildTool buildTool) {
        expect:
        expected == AzureBuildCommandUtils.getBuildCommand(buildTool)

        where:
        expected                              | buildTool
        'mvnw package azure-functions:deploy' | BuildTool.MAVEN
        'gradlew azureFunctionsDeploy'        | BuildTool.GRADLE
        'gradlew azureFunctionsDeploy'        | BuildTool.GRADLE_KOTLIN
    }
}
