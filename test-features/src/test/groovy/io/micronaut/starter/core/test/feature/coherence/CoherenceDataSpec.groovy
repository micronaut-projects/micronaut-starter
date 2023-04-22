package io.micronaut.starter.core.test.feature.coherence

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf
import spock.lang.Unroll

class CoherenceDataSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceData"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void "test maven coherence-data with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["coherence-data"])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    @Unroll
    void "test gradle coherence-data with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["coherence-data"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }
}
