package io.micronaut.starter.core.test.create

import io.micronaut.starter.feature.crac.Crac
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Retry
import spock.lang.Unroll

@Retry // sometimes CI gets connection failure/reset resolving dependencies from Maven central
class CreateCracAppSpec extends CommandSpec {

    void 'test basic create-app for #lang and #buildTool with CRaC'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [Crac.NAME])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-crac-app"
    }
}
