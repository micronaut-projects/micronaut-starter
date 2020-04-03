package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.starter.OutputHandler
import io.micronaut.starter.io.FileSystemOutputHandler
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.nio.file.Files

class CreateAppSpec extends CommandSpec {

    void "test basic create-app"() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateAppCommand command = beanContext.getBean(CreateAppCommand)
        command.name = "foo"
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("run", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 20, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()
        beanContext.close()
    }

}
