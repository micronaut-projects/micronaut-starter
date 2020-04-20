package io.micronaut.starter

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class CodeGenConfigSpec extends Specification {

    @Shared @AutoCleanup BeanContext ctx = BeanContext.run()

    void "test config is compatible with the old format"() {
        when:
        String yml = """
profile: $profile
defaultPackage: micronaut.testing.keycloak
---
testFramework: junit
sourceLanguage: java
"""
        CodeGenConfig config = CodeGenConfig.load(ctx, new ByteArrayInputStream(yml.bytes), ConsoleOutput.NOOP)

        then:
        config.applicationType == command
        config.defaultPackage == "micronaut.testing.keycloak"
        config.testFramework == TestFramework.junit
        config.sourceLanguage == Language.java
        config.buildTool == BuildTool.gradle // picked up because starter is a gradle project
        config.features.containsAll(["java", "junit", "gradle"])

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
