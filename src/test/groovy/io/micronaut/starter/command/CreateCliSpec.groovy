package io.micronaut.starter.command;

import io.micronaut.context.BeanContext;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool;
import spock.lang.Unroll;
import spock.util.concurrent.PollingConditions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

class CreateCliSpec extends CommandSpec {

    @Unroll
    void 'test basic create-cli-app for lang=#lang'() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateCliCommand command = beanContext.getBean(CreateCliCommand)
        command.name = "example.micronaut.foo"
        command.lang = lang
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("run --args=\"-v\"", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("Hi")
        }

        cleanup:
        process.destroy()
        beanContext.close()
        dir.delete()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

    @Unroll
    void 'test basic maven create-cli-app for lang=#lang'() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateCliCommand command = beanContext.getBean(CreateCliCommand)
        command.name = "example.micronaut.foo"
        command.lang = lang
        command.build = BuildTool.maven
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeMavenCommand("compile exec:exec -Dargs=-v", dir)
        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("Hi")
        }

        cleanup:
        process.destroy()
        beanContext.close()
        dir.delete()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

    @Unroll
    void 'test basic create-cli-app test for lang=#lang'() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateCliCommand command = beanContext.getBean(CreateCliCommand)
        command.name = "example.micronaut.foo"
        command.lang = lang
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeGradleCommand("test", dir)

        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("BUILD SUCCESSFUL")
        }

        cleanup:
        process.destroy()
        beanContext.close()
        dir.delete()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }

    @Unroll
    void 'test basic maven create-cli-app test for lang=#lang'() {
        when:
        BeanContext beanContext = BeanContext.run()
        File dir = Files.createTempDirectory("foo").toFile()
        CreateCliCommand command = beanContext.getBean(CreateCliCommand)
        command.name = "example.micronaut.foo"
        command.lang = lang
        command.build = BuildTool.maven
        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Process process = executeMavenCommand("compile test", dir)

        process.consumeProcessOutputStream(baos)

        PollingConditions conditions = new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)

        then:
        conditions.eventually {
            new String(baos.toByteArray()).contains("BUILD SUCCESS")
        }

        cleanup:
        process.destroy()
        beanContext.close()
        dir.delete()

        where:
        lang << ['java', 'groovy', 'kotlin', null]
    }


}
