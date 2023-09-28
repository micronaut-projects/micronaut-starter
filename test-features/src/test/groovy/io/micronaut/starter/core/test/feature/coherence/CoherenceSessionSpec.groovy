package io.micronaut.starter.core.test.feature.coherence

import io.micronaut.starter.feature.coherence.CoherenceSessionStore
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class CoherenceSessionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceSession"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven coherence-session with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [CoherenceSessionStore.NAME])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool coherence-session with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, [CoherenceSessionStore.NAME])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
    }
}
