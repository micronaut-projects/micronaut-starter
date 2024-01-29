package io.micronaut.starter.core.test.create

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.feature.grpc.CreateGrpcServiceCommand
import io.micronaut.starter.cli.feature.grpc.CreateProtoServiceCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.IgnoreIf
import spock.lang.Unroll


class CreateGrpcSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "test-grpc"
    }

    @Unroll
    void 'grpc with #lang and gradle'(Language lang) {
        given:
        BuildTool buildTool = BuildTool.GRADLE
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        ApplicationType applicationType = ApplicationType.GRPC
        generateProject(lang, buildTool, [], applicationType)
        CreateGrpcServiceCommand command = createGrpcServiceCommand(beanContext, consoleOutput)
        command.setBeanContext(beanContext)
        command.serviceName = "Greeting"

        expect:
        command.applies()

        when:
        command.call()

        then:
        1 * consoleOutput.out({ it.contains("Rendered Proto service") })
        1 * consoleOutput.out({ it.contains("Rendered gRPC service") })

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        lang << Language.values()
    }

    @IgnoreIf(value = { os.macOs }, reason = ": Error extracting protoc for version 3.11.4: Unsupported platform: protoc-3.11.4-osx-aarch_64.exe")
    @Unroll
    void 'grpc with #lang and maven'(Language lang) {
        given:
        BuildTool buildTool = BuildTool.MAVEN
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        ApplicationType applicationType = ApplicationType.GRPC
        generateProject(lang, buildTool, [], applicationType)
        CreateGrpcServiceCommand command = createGrpcServiceCommand(beanContext, consoleOutput)
        command.setBeanContext(beanContext)
        command.serviceName = "Greeting"

        expect:
        command.applies()

        when:
        command.call()

        then:
        1 * consoleOutput.out({ it.contains("Rendered Proto service") })
        1 * consoleOutput.out({ it.contains("Rendered gRPC service") })

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        lang << Language.values()
    }

    CreateGrpcServiceCommand createGrpcServiceCommand(BeanContext beanContext,
                                                      ConsoleOutput consoleOutput) {
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)

        CreateProtoServiceCommand protoServiceCommand = new CreateProtoServiceCommand(
                codeGenConfig,
                getOutputHandler(consoleOutput),
                consoleOutput

        )
        protoServiceCommand.setBeanContext(beanContext)

        CreateGrpcServiceCommand command = new CreateGrpcServiceCommand(
                codeGenConfig,
                getOutputHandler(consoleOutput),
                consoleOutput
        ) {
            @Override
            protected CreateProtoServiceCommand getCreateProtoServiceCommand() {
                return protoServiceCommand
            }
        }
        command
    }
}
