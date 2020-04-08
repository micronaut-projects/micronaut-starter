package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.CommandFixture
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
        pollingConditions.eventually {
            new String(baos.toByteArray()).contains("Hi")
        }

        cleanup:
        process.destroy()
        dir.deleteDir()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

    @Unroll
    void 'test basic create-cli-app test for lang=#lang'() {
        given:
        runCreateCliCommand(lang)

        when:
        Process process = executeGradleCommand('test')

        then:
        pollingConditions.eventually {
            new String(baos.toByteArray()).contains("BUILD SUCCESSFUL")
        }

        cleanup:
        process.destroy()
        dir.deleteDir()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }


}
