package io.micronaut.starter.core.test.feature.discovery

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Unroll

class DiscoveryClientFunctionalSpec extends CommandSpec {

    @Unroll
    void 'test discovery-client for #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, ['discovery-client'])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app-discovery-client"
    }
}
