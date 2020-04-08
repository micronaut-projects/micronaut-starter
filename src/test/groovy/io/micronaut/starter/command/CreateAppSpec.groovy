package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.CommandFixture
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
        PollingConditions conditions = defaultPollingConditions
        conditions.eventually {
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()
        dir.deleteDir()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

    @Unroll
    void 'test create-app with feature=graal-native-image for lang=#lang'() {
        given:
        runCreateAppCommand(lang, ['graal-native-image'])

        when:
        Process process = executeGradleCommand('run')

        then:
        PollingConditions conditions = defaultPollingConditions
        conditions.eventually {
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()
        dir.deleteDir()

        where:
        lang << ['java', 'groovy', 'kotlin']
    }

}
