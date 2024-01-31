package io.micronaut.starter.core.test.create


import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.Kapt
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Retry
import spock.lang.Unroll

@Retry // sometimes CI gets connection failure/reset resolving dependencies from Maven central
class CreateCliSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-createcli"
    }

    @Unroll
    void 'create-cli-app with #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        ApplicationType applicationType = ApplicationType.CLI
        generateProject(lang, buildTool, (lang == Language.KOTLIN) ? [Kapt.NAME] : [], applicationType)

        when:
        String output = null
        if (buildTool.isGradle()) {
            output = executeGradle('run', '--args="-v"').output
        } else {
            output = executeMaven("mn:run -Dmn.appArgs=-v")
        }

        then:
        output?.contains("Hi")

        where:
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }
}
