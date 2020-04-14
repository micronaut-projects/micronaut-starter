package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Unroll

class CreateAppSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test basic create-app for lang=#lang'() {
        given:
        runCreateAppCommand(lang)

        when:
        executeGradleCommand('run')

        then:
        testOutputContains("Startup completed")

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

    @Unroll
    void 'test basic create-app for lang=#lang and maven'() {
        given:
        runCreateAppCommand(lang, BuildTool.maven)

        when:
        executeMavenCommand("mn:run")

        then:
        testOutputContains("Startup completed")

        where:
        lang << [Language.java, Language.groovy, Language.kotlin, null]
    }

}
