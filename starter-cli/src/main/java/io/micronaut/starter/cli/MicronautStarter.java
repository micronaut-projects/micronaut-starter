/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.cli;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.starter.ConsoleOutput;
import io.micronaut.starter.cli.command.*;
import picocli.CommandLine;


import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

@CommandLine.Command(name = "mn", description = {
        "Micronaut CLI command line interface for generating projects and services.",
        "Commonly used commands are:",
        "  @|bold create-app|@ @|yellow NAME|@",
        "  @|bold create-cli-app|@ @|yellow NAME|@",
        "  @|bold create-federation|@ @|yellow NAME|@ @|yellow --services|@ @|yellow,italic SERVICE_NAME[,SERVICE_NAME]...|@",
        "  @|bold create-function|@ @|yellow NAME|@"},
        synopsisHeading = "@|bold,underline Usage:|@ ",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        commandListHeading = "%n@|bold,underline Commands:|@%n",
        subcommands = {
                CreateAppCommand.class,
                CreateCliCommand.class,
                CreateGrpcCommand.class
        })
@Prototype
public class MicronautStarter extends BaseCommand implements Callable<Integer> {

    private static Boolean interactiveShell = false;

    private static final BiFunction<Throwable, CommandLine, Integer> EXCEPTION_HANDLER = (e, commandLine) -> {
        BaseCommand command = commandLine.getCommand();
        command.err(e.getMessage());
        if (command.showStacktrace()) {
            e.printStackTrace(commandLine.getErr());
        }
        return 1;
    };

    public static void main(String[] args) {
        if (args.length == 0) {
            //The first command line isn't technically in the shell yet so this is called
            //before setting the static flag
            CommandLine commandLine = createCommandLine();
            MicronautStarter.interactiveShell = true;
            new InteractiveShell(commandLine, MicronautStarter::execute, EXCEPTION_HANDLER).start();
        } else {
            System.exit(execute(args));
        }
    }

    static CommandLine createCommandLine() {
        boolean noOpConsole = MicronautStarter.interactiveShell;
        try (BeanContext beanContext = BeanContext.run()) {
            return createCommandLine(beanContext, noOpConsole);
        }
    }

    static int execute(String[] args) {
        boolean noOpConsole = args.length > 0 && args[0].startsWith("update-cli-config");
        try (BeanContext beanContext = BeanContext.run()) {
            return createCommandLine(beanContext, noOpConsole).execute(args);
        }
    }

    private static CommandLine createCommandLine(BeanContext beanContext, boolean noOpConsole) {
        MicronautStarter starter = beanContext.getBean(MicronautStarter.class);
        CommandLine commandLine = new CommandLine(starter, new MicronautFactory(beanContext));
        commandLine.setExecutionExceptionHandler((ex, commandLine1, parseResult) -> EXCEPTION_HANDLER.apply(ex, commandLine1));
        commandLine.setUsageHelpWidth(100);

        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, noOpConsole ? ConsoleOutput.NOOP : starter);
        if (codeGenConfig != null) {
            beanContext.getBeanDefinitions(CodeGenCommand.class).stream()
                    .map(BeanDefinition::getBeanType)
                    .map(bt -> beanContext.createBean(bt, codeGenConfig))
                    .filter(CodeGenCommand::applies)
                    .forEach(commandLine::addSubcommand);
        }

        return commandLine;
    }

    @Override
    public Integer call() throws Exception {
        throw new CommandLine.ParameterException(spec.commandLine(), "No command specified");
    }
}
