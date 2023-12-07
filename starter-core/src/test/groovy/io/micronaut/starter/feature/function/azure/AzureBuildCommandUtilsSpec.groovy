package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.options.BuildTool
import spock.lang.Specification
import spock.lang.Unroll

class AzureBuildCommandUtilsSpec extends Specification {
    @Unroll("For #buildTool build command is #expected")
    void "azure build command"(String expected, BuildTool buildTool) {
        expect:
        expected == AzureBuildCommandUtils.getBuildCommand(buildTool)

        where:
        expected | buildTool
        'mvnw package azure-functions:deploy' | BuildTool.MAVEN
        'gradlew azureFunctionsDeploy' | BuildTool.GRADLE
        'gradlew azureFunctionsDeploy' | BuildTool.GRADLE_KOTLIN
    }
}
