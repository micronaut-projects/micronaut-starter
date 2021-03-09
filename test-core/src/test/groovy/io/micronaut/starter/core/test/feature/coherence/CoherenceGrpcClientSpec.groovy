package io.micronaut.starter.core.test.feature.coherence

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class CoherenceGrpcClientSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceGrpcClient"
    }

    @Unroll
    void "test maven coherence-grpc-client with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["coherence-grpc-client"])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle coherence-grpc-client with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["coherence-grpc-client"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
