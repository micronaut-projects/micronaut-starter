package io.micronaut.starter.core.test.feature.cache

import io.micronaut.starter.feature.cache.Coherence
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class CoherenceSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceCache"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven coherence-cache with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [Coherence.NAME])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool coherence-cache with #language"(BuildTool buildTool, Language language) {
        when:
        println dir
        generateProject(language, buildTool, [Coherence.NAME])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, language] << [BuildTool.valuesGradle(), Language.values()].combinations()
    }
}
