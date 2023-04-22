package io.micronaut.starter.core.test.feature.cache

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

class CoherenceSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceCache"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven coherence-cache with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["cache-coherence"])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle coherence-cache with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["cache-coherence"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
