package io.micronaut.starter.command

import io.micronaut.starter.OutputHandler
import io.micronaut.starter.io.FileSystemOutputHandler
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import java.nio.file.Files

class CreateAppSpec extends CommandSpec {

    @Unroll
    void 'test basic create-app for lang=#lang'() {
        when:
        File dir = Files.createTempDirectory("foo").toFile()
        CreateAppCommand command = new CreateAppCommand(name: "example.micronaut.foo", lang: lang)
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("run", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            process.consumeProcessOutputStream(baos)
            new String(baos.toByteArray()).contains("Startup completed")
        }

        cleanup:
        process.destroy()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

}
