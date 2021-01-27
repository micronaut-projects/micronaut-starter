package io.micronaut.starter.core.test.create

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.feature.grpc.CreateGrpcServiceCommand
import io.micronaut.starter.cli.feature.grpc.CreateProtoServiceCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Retry
import spock.lang.Unroll

@Retry // sometimes CI gets connection failure/reset resolving dependencies from Maven central
class CreateGrpcSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "test-grpc"
    }

    @Unroll
    void 'grpc with #lang and #buildTool'(Language lang, BuildTool buildTool) {
        given:
        ApplicationType applicationType = ApplicationType.GRPC
        generateProject(lang, buildTool, [], applicationType)

        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)

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
        [lang, buildTool] << LanguageBuildCombinations.combinations()
    }
}
