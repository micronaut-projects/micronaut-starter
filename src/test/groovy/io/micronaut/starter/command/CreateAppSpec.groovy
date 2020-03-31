package io.micronaut.starter.command

import io.micronaut.starter.OutputHandler
import io.micronaut.starter.io.FileSystemOutputHandler
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.nio.file.Files
import java.nio.file.Path

class CreateAppSpec extends CommandSpec {

    void "test basic create-app"() {
        when:
        File dir = Files.createTempDirectory("foo").toFile()
        CreateAppCommand command = new CreateAppCommand(name: "foo")
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("run", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 20, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            process.consumeProcessOutputStream()
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()
    }

}
