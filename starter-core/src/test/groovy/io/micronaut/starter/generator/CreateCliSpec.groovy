package io.micronaut.starter.generator

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateCliSpec extends CommandSpec {

    @Unroll
    void 'test basic create-cli-app for lang=#lang'() {
        given:
        generateCliProject(new Options(lang, null, BuildTool.GRADLE))

        when:
        executeGradleCommand('run --args="-v"')

        then:
        testOutputContains("Hi")

        where:
        lang << [Language.JAVA, Language.GROOVY, Language.KOTLIN, null]
    }

    @Unroll
    void 'test basic maven create-cli-app for lang=#lang'() {
        given:
        generateCliProject(new Options(lang, null, BuildTool.MAVEN))

        when:
        executeMavenCommand("mn:run -Dmn.appArgs=-v")

        then:
        testOutputContains("Hi")

        where:
        lang << [Language.JAVA, Language.GROOVY, Language.KOTLIN, null]
    }

    @Unroll
    void 'test basic create-cli-app test for lang=#lang and #testFramework'() {
        given:
        generateCliProject(new Options(lang, testFramework, BuildTool.GRADLE))

        when:
        executeGradleCommand('test')

        then:
        testOutputContains("BUILD SUCCESSFUL")

        where:
        lang            | testFramework
        Language.JAVA   | TestFramework.JUNIT
        Language.GROOVY | TestFramework.JUNIT
        Language.KOTLIN | TestFramework.JUNIT
        Language.GROOVY | TestFramework.SPOCK
        Language.KOTLIN | TestFramework.KOTLINTEST
    }

    @Unroll
    void 'test basic maven create-cli-app test for lang=#lang'() {
        given:
        generateCliProject(new Options(lang, testFramework, BuildTool.MAVEN))

        when:
        executeMavenCommand("compile test")

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        lang            | testFramework
        Language.JAVA   | TestFramework.JUNIT
        Language.GROOVY | TestFramework.JUNIT
        Language.KOTLIN | TestFramework.JUNIT
        Language.GROOVY | TestFramework.SPOCK
        Language.KOTLIN | TestFramework.KOTLINTEST
    }


}
