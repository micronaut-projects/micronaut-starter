package io.micronaut.starter.cli.command

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.AutoCleanup
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Specification

class CreateAppCommandSpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext ctx = ApplicationContext.run(Environment.CLI)

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/352")
    void "test micronaut is not a valid application name"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setErr(new PrintStream(baos))

        when:
        PicocliRunner.run(CreateAppCommand, ctx, "micronaut")

        then:
        noExceptionThrown()
        baos.toString().contains("micronaut is not a valid app name")
    }
}
