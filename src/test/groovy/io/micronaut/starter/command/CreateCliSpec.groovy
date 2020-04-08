package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Unroll

class CreateCliSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test basic create-cli-app for lang=#lang'() {
        given:
        runCreateCliCommand(lang)

        when:
        Process process = executeGradleCommand('run --args="-v"')

        then:
        testOutputContains("Hi")

        cleanup:
        process.destroy()

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

    @Unroll
    void 'test basic maven create-cli-app for lang=#lang'() {
        given:
        runCreateCliCommand(lang, BuildTool.maven)

        when:
        Process process = executeMavenCommand("compile exec:exec -Dargs=-v")

        then:
        testOutputContains("Hi")

        cleanup:
        process.destroy()

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

    @Unroll
    void 'test basic create-cli-app test for lang=#lang'() {
        given:
        runCreateCliCommand(lang)

        when:
        Process process = executeGradleCommand('test')

        then:
        testOutputContains("BUILD SUCCESSFUL")

        cleanup:
        process.destroy()

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

    @Unroll
    void 'test basic maven create-cli-app test for lang=#lang'() {
        given:
        runCreateCliCommand(lang, BuildTool.maven)

        when:
        Process process = executeMavenCommand("compile test")

        then:
        testOutputContains("BUILD SUCCESS")

        cleanup:
        process.destroy()

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }


}
