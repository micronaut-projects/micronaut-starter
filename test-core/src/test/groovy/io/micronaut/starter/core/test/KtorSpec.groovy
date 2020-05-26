package io.micronaut.starter.core.test

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.PendingFeature
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

class KtorSpec extends CommandSpec {

    @Override
    PollingConditions getDefaultPollingConditions() {
        new PollingConditions(timeout: 15)
    }

    @PendingFeature(reason = "micronaut-kotlin is not in maven central")
    @Unroll
    void 'create-app with feature ktor for #lang and #buildTool starts successfully'(Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, ['ktor'] as List<String>, ApplicationType.DEFAULT)

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        [lang, buildTool] << [[Language.KOTLIN], BuildToolCombinations.buildTools].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        'test-ktor'
    }
}
