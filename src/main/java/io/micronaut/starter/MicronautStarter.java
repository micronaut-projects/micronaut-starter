package io.micronaut.starter;

import io.micronaut.starter.command.CreateAppCommand;
import picocli.CommandLine;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.shell.jline3.PicocliJLineCompleter;

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
                CreateAppCommand.class
        })
public class MicronautStarter extends BaseCommand implements Callable<Integer> {

    private static final BiFunction<Throwable, CommandLine, Integer> exceptionHandler = (e, commandLine) -> {
        BaseCommand command = commandLine.getCommand();
        command.err(e.getMessage());
        if (command.showStacktrace()) {
            e.printStackTrace(commandLine.getErr());
        }
        return 1;
    };

    public static void main(String[] args) {
        if (args.length == 0) {
            new InteractiveShell(MicronautStarter::createCommandLine, exceptionHandler).start();
        } else {
            System.exit(createCommandLine().execute(args));
        }
    }

    static CommandLine createCommandLine() {
        MicronautStarter starter = new MicronautStarter();
        CommandLine commandLine = new CommandLine(starter);
        commandLine.setExecutionExceptionHandler((ex, commandLine1, parseResult) -> exceptionHandler.apply(ex, commandLine1));
        commandLine.setUsageHelpWidth(100);
        return commandLine;
    }

    @Override
    public Integer call() throws Exception {
        throw new CommandLine.ParameterException(spec.commandLine(), "No command specified");
    }
}
