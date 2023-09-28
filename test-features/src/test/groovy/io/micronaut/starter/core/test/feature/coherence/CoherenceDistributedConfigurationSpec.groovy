package io.micronaut.starter.core.test.feature.coherence

import io.micronaut.starter.feature.coherence.CoherenceDistributedConfiguration
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class CoherenceDistributedConfigurationSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "coherenceDistributedConfig"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven coherence-distributed-configuration with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [CoherenceDistributedConfiguration.NAME])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool coherence-distributed-configuration with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, [CoherenceDistributedConfiguration.NAME])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, language] << [BuildTool.valuesGradle(), Language.values()].combinations()
    }
}
