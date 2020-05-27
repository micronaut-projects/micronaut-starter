package io.micronaut.starter.core.test.create

import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateAppSpec extends CommandSpec {

    @Unroll
    void 'test basic create-app for #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [])

        when:
        String output = executeBuild(buildTool, buildTool == BuildTool.GRADLE ? "run" : "mn:run")

        then:
        output.contains("Startup completed")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app"
    }
}
