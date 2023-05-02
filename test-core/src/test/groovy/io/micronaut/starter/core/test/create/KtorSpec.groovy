package io.micronaut.starter.core.test.create

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.kotlin.Ktor
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec

class KtorSpec extends CommandSpec {

    void 'create-app with feature ktor for #lang and #buildTool starts successfully'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [Ktor.NAME] as List<String>, ApplicationType.DEFAULT)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool] << [[Language.KOTLIN], BuildToolCombinations.buildTools].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        'test-ktor'
    }
}
