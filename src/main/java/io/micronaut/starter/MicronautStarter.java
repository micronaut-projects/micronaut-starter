package io.micronaut.starter;

import io.micronaut.starter.command.CreateAppCommand;
import org.jline.builtins.Widgets;
import org.jline.reader.impl.LineReaderImpl;
import picocli.CommandLine;
import org.fusesource.jansi.AnsiConsole;
import org.jline.builtins.Builtins;
import org.jline.builtins.Completers.SystemCompleter;
import org.jline.builtins.Options.HelpException;
import org.jline.builtins.Widgets.TailTipWidgets;
import org.jline.builtins.Widgets.TailTipWidgets.TipType;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import picocli.shell.jline3.PicocliCommands;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
public class MicronautStarter extends BaseCommand {

    public static void main(String[] args) {
        MicronautStarter starter = new MicronautStarter();
        CommandLine commandLine = createCommandLine();
        if (args.length == 0) {
            startInteractiveConsole(commandLine);
        } else {
            System.exit(commandLine.execute(args));
        }
    }

    static CommandLine createCommandLine() {
        MicronautStarter starter = new MicronautStarter();
        CommandLine commandLine = new CommandLine(starter);
        commandLine.setExecutionExceptionHandler((ex, commandLine1, parseResult) -> {
            BaseCommand command = commandLine1.getCommand();
            command.err(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (command.showStacktrace()) {
                ex.printStackTrace(commandLine1.getErr());
            }
            return 1;
        });
        commandLine.setUsageHelpWidth(100);
        return commandLine;
    }

    static void startInteractiveConsole(CommandLine cmd) {
        AnsiConsole.systemInstall();
        try {
            // set up JLine built-in commands
            Path workDir = Paths.get("");
            Builtins builtins = new Builtins(workDir, null, null);
            builtins.rename(org.jline.builtins.Builtins.Command.TTOP, "top");
            builtins.alias("zle", "widget");
            builtins.alias("bindkey", "keymap");
            SystemCompleter systemCompleter = builtins.compileCompleters();
            // set up picocli commands
            PicocliCommands picocliCommands = new PicocliCommands(workDir, cmd);
            systemCompleter.add(picocliCommands.compileCompleters());
            systemCompleter.compile();
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(systemCompleter)
                    .parser(new DefaultParser())
                    .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                    .build();
            builtins.setLineReader(reader);
            DescriptionGenerator descriptionGenerator = new DescriptionGenerator(builtins, picocliCommands);
            new TailTipWidgets(reader, descriptionGenerator::commandDescription, 5, TipType.COMPLETER);

            String prompt = CommandLine.Help.Ansi.AUTO.string("@|blue mn> |@ ");
            String rightPrompt = null;

            // start the shell and process input until the user quits with Ctl-D
            String line;
            while (true) {
                try {
                    line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
                    if (line.matches("^\\s*#.*")) {
                        continue;
                    }
                    if (line.equals("exit")) {
                        return;
                    }
                    ParsedLine pl = reader.getParser().parse(line, 0);
                    String[] arguments = pl.words().toArray(new String[0]);
                    String command = Parser.getCommand(pl.word());
                    if (builtins.hasCommand(command)) {
                        builtins.execute(command, Arrays.copyOfRange(arguments, 1, arguments.length)
                                , System.in, System.out, System.err);
                    } else {
                        createCommandLine().execute(arguments);
                    }
                } catch (HelpException e) {
                   // HelpException.highlight(e.getMessage(), HelpException.defaultStyle()).print(terminal);
                } catch (UserInterruptException | EndOfFileException e) {
                    return;
                } catch (Exception e) {
                   // AttributedStringBuilder asb = new AttributedStringBuilder();
                   // asb.append(e.getMessage(), AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
                   // asb.toAttributedString().println(terminal);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    /**
     * Provide command descriptions for JLine TailTipWidgets
     * to be displayed in the status bar.
     */
    private static class DescriptionGenerator {
        Builtins builtins;
        PicocliCommands picocli;

        public DescriptionGenerator(Builtins builtins, PicocliCommands picocli) {
            this.builtins = builtins;
            this.picocli = picocli;
        }

        Widgets.CmdDesc commandDescription(Widgets.CmdLine line) {
            Widgets.CmdDesc out = null;
            switch (line.getDescriptionType()) {
                case COMMAND:
                    String cmd = Parser.getCommand(line.getArgs().get(0));
                    if (builtins.hasCommand(cmd)) {
                        out = builtins.commandDescription(cmd);
                    } else if (picocli.hasCommand(cmd)) {
                        out = picocli.commandDescription(cmd);
                    }
                    break;
                default:
                    break;
            }
            return out;
        }
    }

}
