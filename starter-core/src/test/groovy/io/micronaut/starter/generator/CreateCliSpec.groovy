package io.micronaut.starter.generator

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateCliSpec extends CommandSpec {

    @Unroll
    void 'test basic create-cli-app for #language and #buildTool'(Language language, BuildTool buildTool) {
        given:
        generateCliProject(new Options(language, null, buildTool))

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run --args="-v"')
        } else {
            executeMavenCommand("mn:run -Dmn.appArgs=-v")
        }

        then:
        testOutputContains("Hi")

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    @Unroll
    void 'test basic create-cli-app test for #language and #testFramework and #buildTool'(Language language, TestFramework testFramework, BuildTool buildTool) {
        given:
        generateCliProject(new Options(language, testFramework, buildTool))

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('test')
        } else {
            executeMavenCommand("compile test")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        [language, testFramework, buildTool] << [Language.values(), TestFramework.values(), BuildTool.values()].combinations()
    }

}
