package io.micronaut.starter.core.test.create

import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Retry
import spock.lang.Unroll

@Retry // sometimes CI gets connection failure/reset resolving dependencies from Maven central
class CreateAppSpec extends CommandSpec {

    @Unroll
    void 'test basic create-app for #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app"
    }
}
