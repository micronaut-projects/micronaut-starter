package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

class CreateAppSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test basic create-app for lang=#lang'() {
        given:
        runCreateAppCommand(lang)

        when:
        Process process = executeGradleCommand('run')

        then:
        testOutputContains("Startup completed")

        cleanup:
        process.destroy()

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

    @Unroll
    void 'test basic create-app for lang=#lang and maven'() {
        given:
        runCreateAppCommand(lang, BuildTool.maven)

        when:
        Process process = executeMavenCommand("compile exec:exec")

        then:
        testOutputContains("Startup completed")

        cleanup:
        process.destroy()

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

}
