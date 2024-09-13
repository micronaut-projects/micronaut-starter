package io.micronaut.starter.core.test.feature.tracing

import io.micronaut.starter.feature.tracing.Zipkin
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations

class ZipkinSpec extends CommandSpec {

    void 'test zipkin feature for #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        println dir
        generateProject(lang, buildTool, [Zipkin.NAME])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "zipkin-tracing-app"
    }
}
