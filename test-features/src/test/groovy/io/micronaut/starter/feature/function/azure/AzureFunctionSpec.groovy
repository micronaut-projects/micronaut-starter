package io.micronaut.starter.feature.function.azure

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import spock.lang.Unroll
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult

class AzureFunctionSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "azureFunctionSpec"
    }

    @Unroll
    void "test azure-function feature with #buildTool"(BuildTool buildTool) {
        when:
        generateProject(Language.JAVA, buildTool, ["azure-function"], ApplicationType.FUNCTION)
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        buildTool <<  BuildTool.valuesGradle()
    }
}
