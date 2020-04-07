package io.micronaut.starter;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.command.BaseCommand;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCliCommand;
import picocli.CommandLine;

import javax.inject.Singleton;
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
                CreateCliCommand.class
        })
@Prototype
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
            new InteractiveShell(createCommandLine(), MicronautStarter::execute, exceptionHandler).start();
        } else {
            System.exit(execute(args));
        }
    }

    static CommandLine createCommandLine() {
        try (BeanContext beanContext = BeanContext.run()) {
            return createCommandLine(beanContext);
        }
    }

    static int execute(String[] args) {
        try (BeanContext beanContext = BeanContext.run()) {
            return createCommandLine(beanContext).execute(args);
        }
    }

    private static CommandLine createCommandLine(BeanContext beanContext) {
        MicronautStarter starter = beanContext.getBean(MicronautStarter.class);
        CommandLine commandLine = new CommandLine(starter, new CommandLine.IFactory() {
            CommandLine.IFactory defaultFactory = CommandLine.defaultFactory();

            @Override
            public <K> K create(Class<K> cls) throws Exception {
                Optional<K> bean = beanContext.findOrInstantiateBean(cls);
                if (bean.isPresent()) {
                    return bean.get();
                } else {
                    return defaultFactory.create(cls);
                }
            }
        });
        commandLine.setExecutionExceptionHandler((ex, commandLine1, parseResult) -> exceptionHandler.apply(ex, commandLine1));
        commandLine.setUsageHelpWidth(100);
        return commandLine;
    }

    @Override
    public Integer call() throws Exception {
        throw new CommandLine.ParameterException(spec.commandLine(), "No command specified");
    }
}
