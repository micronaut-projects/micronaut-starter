package io.micronaut.starter.core.test.feature.coherence

import io.micronaut.starter.feature.coherence.CoherenceGrpcClient
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class CoherenceGrpcClientSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceGrpcClient"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven coherence-grpc-client with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [CoherenceGrpcClient.NAME])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool coherence-grpc-client with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, [CoherenceGrpcClient.NAME])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
    }
}
