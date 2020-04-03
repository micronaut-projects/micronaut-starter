package io.micronaut.starter;

import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliJLineCompleter;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InteractiveShell {

    private final CommandLine commandLine;
    private final Consumer<String[]> executor;
    private final BiFunction<Throwable, CommandLine, Integer> onError;

    InteractiveShell(CommandLine commandLine,
                     Consumer<String[]> executor,
                     BiFunction<Throwable, CommandLine, Integer> onError) {
        this.commandLine = commandLine;
        this.executor = executor;
        this.onError = onError;
    }

    void start() {
        AnsiConsole.systemInstall();
        try {
            PicocliJLineCompleter picocliCommands = new PicocliJLineCompleter(commandLine.getCommandSpec());
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(picocliCommands)
                    .parser(new DefaultParser())
                    .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                    .build();

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
                    executor.accept(arguments);
                } catch (UserInterruptException | EndOfFileException e) {
                    return;
                }
            }
        } catch (Throwable t) {
            onError.apply(t, commandLine);
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}
