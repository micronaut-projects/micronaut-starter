package io.micronaut.starter.cli.command

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class AddFeatureCommandSpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext ctx = ApplicationContext.run(Environment.CLI)

    void "test arm is not an OpenRewriteFeature"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setErr(new PrintStream(baos))
        String featureName = "arm"
        when:
        PicocliRunner.run(AddFeatureCommand, ctx, "--features", featureName)

        then:
        noExceptionThrown()
        baos.toString().contains("Feature [${featureName}] is not supported by the add-feature command")
    }

    void "apply mockito OpenRewriteFeature"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setErr(new PrintStream(baos))
        String featureName = "mockito"

        when:
        PicocliRunner.run(AddFeatureCommand, ctx, "--features", featureName)

        then:
        noExceptionThrown()
        !baos.toString().contains("Feature [${featureName}] is not supported by the add-feature command")
    }
}
