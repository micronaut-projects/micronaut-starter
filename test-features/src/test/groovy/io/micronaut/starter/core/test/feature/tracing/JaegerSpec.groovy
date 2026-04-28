package io.micronaut.starter.core.test.feature.tracing

import io.micronaut.starter.feature.tracing.Jaeger
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations

class JaegerSpec extends CommandSpec {

    void 'test jaeger feature for #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [Jaeger.NAME])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "jaeger-tracing-app"
    }
}
