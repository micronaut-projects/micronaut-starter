package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.OutputHandler
import io.micronaut.starter.io.FileSystemOutputHandler
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import java.nio.file.Files

class CreateAppSpec extends CommandSpec {

    @Unroll
    void 'test basic create-app for lang=#lang'() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateAppCommand command = beanContext.getBean(CreateAppCommand)
        command.name = "example.micronaut.foo"
        command.lang = lang
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("run", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()
        beanContext.close()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

    @Unroll
    void 'test create-app with feature=graal-native-image for lang=#lang'() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateAppCommand command = beanContext.getBean(CreateAppCommand)
        command.name = "example.micronaut.foo"
        command.lang = lang
        command.features = ['graal-native-image']
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("run", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()
        beanContext.close()

        where:
        lang << ['java', 'groovy', 'kotlin']
    }

}
