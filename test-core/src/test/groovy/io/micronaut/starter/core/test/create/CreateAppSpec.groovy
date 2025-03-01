package io.micronaut.starter.core.test.create

import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Unroll

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
