package io.micronaut.starter.cli

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files

class CodeGenConfigSpec extends Specification {

    @Shared @AutoCleanup BeanContext ctx = BeanContext.run()

    void "test config is compatible with the old format"() {
        when:
        File dir = Files.createTempDirectory("cli-config").toFile()
        new File(dir, "micronaut-cli.yml").write("""
profile: $profile
defaultPackage: micronaut.testing.keycloak
---
testFramework: junit
sourceLanguage: java
""")
        new File(dir, "build.gradle").createNewFile()

        CodeGenConfig config = CodeGenConfig.load(ctx, dir, ConsoleOutput.NOOP)

        then:
        config.applicationType == command
        config.defaultPackage == "micronaut.testing.keycloak"
        config.testFramework == TestFramework.JUNIT
        config.sourceLanguage == Language.JAVA
        config.buildTool.isGradle()
        config.features.containsAll(["java", "gradle"])

        cleanup:
        dir.delete()

        where:
        profile              | command
        "service"            | ApplicationType.DEFAULT
        "cli"                | ApplicationType.CLI
        "function-aws"       | ApplicationType.FUNCTION
        "function-aws-alexa" | ApplicationType.FUNCTION
        "grpc"               | ApplicationType.GRPC
        "kafka"              | ApplicationType.MESSAGING
        "rabbitmq"           | ApplicationType.MESSAGING
    }
}
