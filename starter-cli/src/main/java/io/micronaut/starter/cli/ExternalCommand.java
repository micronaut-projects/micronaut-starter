/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.cli;

import io.micronaut.core.annotation.Internal;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine;
import picocli.CommandLine.Model.OptionSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * An executable named {@code mn-<name>} on {@code PATH} that extends {@code mn} with a
 * {@code <name>} command, following the {@code git-<name>} and {@code kubectl-<name>}
 * convention.
 *
 * <p>The lookup runs when the first command name after the root options is not a command
 * or alias registered in {@code mn}; built-in commands therefore always win. The root
 * options in front of the name are consumed by {@code mn} and are not passed on. The name
 * must match {@code [a-z][a-z0-9-]*}, so path separators, dots and upper-case names are
 * never looked up. Only the directories listed in {@code PATH} are searched; empty entries
 * (the current directory on some shells) are skipped. On Windows the extensions in
 * {@code PATHEXT} are tried in order.</p>
 *
 * <p>The external command inherits the standard streams and its exit code becomes the
 * exit code of {@code mn}. Only {@link ProcessBuilder} is used, so the lookup works in the
 * native {@code mn} image without further configuration. On Windows, {@code .cmd} and
 * {@code .bat} files run through the command processor, which parses the command line
 * again; they are refused when their path or an argument contains a character that
 * could not be passed literally (see {@link #batchRefusal}). Other executables are
 * started directly.</p>
 *
 * @since 5.3.0
 */
@Internal
final class ExternalCommand {

    static final String PREFIX = "mn-";
    static final int CANNOT_RUN = 127;

    private static final Pattern NAME = Pattern.compile("[a-z][a-z0-9-]*");
    private static final String DEFAULT_PATHEXT = ".EXE;.CMD;.BAT";
    /**
     * Characters that cmd.exe interprets inside the command line of a batch file
     * (quoting, environment variable and delayed expansion, redirection, pipes, escapes
     * and command separators) in addition to control characters.
     */
    private static final String BATCH_ARGUMENT_METACHARACTERS = "\"%&()<>^|!";
    /**
     * The path is always quoted on the command line, so only the characters that quoting
     * cannot protect are refused, in addition to control characters.
     */
    private static final String BATCH_PATH_METACHARACTERS = "\"%!";

    private final String name;
    private final Path executable;
    private final List<String> arguments;

    private ExternalCommand(String name, Path executable, List<String> arguments) {
        this.name = name;
        this.executable = executable;
        this.arguments = arguments;
    }

    /**
     * @return the command name without the {@code mn-} prefix
     */
    @NonNull
    String getName() {
        return name;
    }

    /**
     * @return the executable that will run
     */
    @NonNull
    Path getExecutable() {
        return executable;
    }

    /**
     * @return the arguments passed to the executable
     */
    @NonNull
    List<String> getArguments() {
        return arguments;
    }

    /**
     * Finds the external command for an invocation.
     *
     * @param root        the {@code mn} command line with every built-in subcommand registered
     * @param args        the arguments passed to {@code mn}
     * @param environment the process environment ({@code PATH} and, on Windows, {@code PATHEXT})
     * @return the external command, or empty when {@code args} name an option, a built-in
     * command, an invalid name, or a name with no executable on {@code PATH}
     */
    @NonNull
    static Optional<ExternalCommand> find(@NonNull CommandLine root, @NonNull String[] args, @NonNull Map<String, String> environment) {
        int commandIndex = commandIndex(root, args);
        if (commandIndex < 0) {
            return Optional.empty();
        }
        String candidate = args[commandIndex];
        if (!NAME.matcher(candidate).matches() || root.getSubcommands().containsKey(candidate)) {
            return Optional.empty();
        }
        return locate(candidate, environment)
                .map(executable -> new ExternalCommand(candidate, executable,
                        Arrays.asList(args).subList(commandIndex + 1, args.length)));
    }

    /**
     * Finds the first token that can name a subcommand. Root options that take no value
     * ({@code -x}, {@code -v} and their long forms, optionally with an attached
     * {@code true} or {@code false}) are skipped and are not passed on, and {@code --}
     * ends the options. Everything else that starts with a dash is left to Picocli:
     * help and version options keep their precedence, and unknown options, clustered
     * short options such as {@code -xv} and options that take a value are not skipped.
     *
     * @return the index of the candidate command name, or -1 when Picocli handles the arguments
     */
    private static int commandIndex(@NonNull CommandLine root, @NonNull String[] args) {
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            if ("--".equals(argument)) {
                return i + 1 < args.length ? i + 1 : -1;
            }
            if (!argument.startsWith("-")) {
                return i;
            }
            int valueSeparator = argument.indexOf('=');
            String optionName = valueSeparator > 0 ? argument.substring(0, valueSeparator) : argument;
            OptionSpec option = root.getCommandSpec().findOption(optionName);
            if (option == null || option.usageHelp() || option.versionHelp() || option.arity().max() > 0) {
                return -1;
            }
            // Boolean flags accept an attached true/false value. Let Picocli
            // produce its normal usage error for every other attached value.
            if (valueSeparator > 0 && !(isBooleanOption(option) && isBooleanValue(argument.substring(valueSeparator + 1)))) {
                return -1;
            }
        }
        return -1;
    }

    private static boolean isBooleanOption(OptionSpec option) {
        return option.type() == boolean.class || option.type() == Boolean.class;
    }

    private static boolean isBooleanValue(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    /**
     * Locates the executable for a command name.
     *
     * @param name        the command name without the {@code mn-} prefix
     * @param environment the process environment
     * @return the first matching executable on {@code PATH}
     */
    @NonNull
    static Optional<Path> locate(@NonNull String name, @NonNull Map<String, String> environment) {
        if (!NAME.matcher(name).matches()) {
            return Optional.empty();
        }
        List<String> extensions = extensions(environment);
        for (Path directory : searchPath(environment)) {
            for (String extension : extensions) {
                Path candidate = directory.resolve(PREFIX + name + extension);
                if (Files.isRegularFile(candidate) && Files.isExecutable(candidate)) {
                    return Optional.of(candidate);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Lists the external commands available on {@code PATH}, for {@code mn --help}.
     *
     * @param environment the process environment
     * @return the sorted command names, without the {@code mn-} prefix
     */
    @NonNull
    static List<String> available(@NonNull Map<String, String> environment) {
        TreeSet<String> names = new TreeSet<>();
        List<String> extensions = extensions(environment);
        for (Path directory : searchPath(environment)) {
            if (!Files.isDirectory(directory)) {
                continue;
            }
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(directory, PREFIX + "*")) {
                for (Path entry : entries) {
                    externalName(entry, extensions).ifPresent(names::add);
                }
            } catch (IOException | RuntimeException ignored) {
                // An unreadable PATH entry (including a DirectoryIteratorException
                // raised while listing it) does not affect help output.
            }
        }
        return new ArrayList<>(names);
    }

    /**
     * Runs the external command with inherited standard streams.
     *
     * @return the command's exit code, or {@value #CANNOT_RUN} when it cannot be started
     */
    int run() {
        if (isWindowsBatchFile(executable)) {
            Optional<String> refusal = batchRefusal(executable, arguments);
            if (refusal.isPresent()) {
                System.err.println("mn: cannot run " + executable + ": " + refusal.get()
                        + ", which cmd.exe would interpret instead of passing it literally");
                return CANNOT_RUN;
            }
        }
        Process process;
        try {
            process = new ProcessBuilder(processCommand()).inheritIO().start();
        } catch (IOException | RuntimeException e) {
            // RuntimeException covers the arguments the JDK refuses to quote on Windows.
            System.err.println("mn: cannot run " + executable + ": " + e.getMessage());
            return CANNOT_RUN;
        }
        try {
            return process.waitFor();
        } catch (InterruptedException e) {
            // Stop the processes the command started as well, not only the command itself.
            List<ProcessHandle> descendants = process.descendants().toList();
            descendants.forEach(ProcessHandle::destroyForcibly);
            process.destroyForcibly();
            Thread.currentThread().interrupt();
            System.err.println("mn: interrupted while running " + executable);
            return CANNOT_RUN;
        }
    }

    /**
     * Checks whether cmd.exe could pass a path and arguments literally to a batch file.
     * Every argument is wrapped in double quotes, but cmd.exe still expands environment
     * variables and delayed expansion inside quotes, and a batch file that reads an
     * argument such as {@code %~1} inserts it into a command line that is parsed again.
     * Arguments containing a double quote, {@code % & ( ) < > ^ | !} or a control
     * character are therefore refused, and so are paths containing a double quote,
     * {@code %}, {@code !} or a control character.
     *
     * @param executable the batch file
     * @param arguments  the arguments for the batch file
     * @return a description of the first offending character, or empty when the batch file can run
     */
    @NonNull
    static Optional<String> batchRefusal(@NonNull Path executable, @NonNull List<String> arguments) {
        int character = firstUnsafeCharacter(executable.toString(), BATCH_PATH_METACHARACTERS);
        if (character >= 0) {
            return Optional.of("the path of the batch file contains " + describe(character));
        }
        for (int i = 0; i < arguments.size(); i++) {
            character = firstUnsafeCharacter(arguments.get(i), BATCH_ARGUMENT_METACHARACTERS);
            if (character >= 0) {
                return Optional.of("argument " + (i + 1) + " contains " + describe(character));
            }
        }
        return Optional.empty();
    }

    private static int firstUnsafeCharacter(String value, String metacharacters) {
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character < 0x20 || metacharacters.indexOf(character) >= 0) {
                return character;
            }
        }
        return -1;
    }

    private static String describe(int character) {
        return character < 0x20
                ? String.format("a control character (U+%04X)", character)
                : "the command-shell metacharacter '" + (char) character + "'";
    }

    private List<String> processCommand() {
        List<String> commandLine = new ArrayList<>(arguments.size() + 4);
        if (isWindowsBatchFile(executable)) {
            // CreateProcess cannot execute a .cmd/.bat file directly. Pass one
            // quoted command string so cmd.exe preserves a path containing spaces:
            // with /s it removes the outer quotes and parses the rest as written.
            commandLine.add(commandProcessor());
            commandLine.add("/d");
            commandLine.add("/s");
            commandLine.add("/c");
            StringBuilder command = new StringBuilder("\"");
            appendQuoted(command, executable.toString());
            for (String argument : arguments) {
                command.append(' ');
                appendQuoted(command, argument);
            }
            commandLine.add(command.append('"').toString());
        } else {
            commandLine.add(executable.toString());
            commandLine.addAll(arguments);
        }
        return commandLine;
    }

    private static void appendQuoted(StringBuilder command, String argument) {
        command.append('"').append(argument).append('"');
    }

    private static String commandProcessor() {
        String commandProcessor = System.getenv("ComSpec");
        return commandProcessor == null || commandProcessor.isBlank() ? "cmd.exe" : commandProcessor;
    }

    private static boolean isWindows() {
        return File.separatorChar == '\\';
    }

    private static boolean isWindowsBatchFile(Path path) {
        if (!isWindows()) {
            return false;
        }
        String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".cmd") || fileName.endsWith(".bat");
    }

    private static Optional<String> externalName(Path entry, List<String> extensions) {
        if (!Files.isRegularFile(entry) || !Files.isExecutable(entry)) {
            return Optional.empty();
        }
        String fileName = entry.getFileName().toString();
        boolean prefixMatches = isWindows()
                ? fileName.regionMatches(true, 0, PREFIX, 0, PREFIX.length())
                : fileName.startsWith(PREFIX);
        if (!prefixMatches) {
            return Optional.empty();
        }
        String lowerCaseFileName = fileName.toLowerCase(Locale.ROOT);
        for (String extension : extensions) {
            if (fileName.length() > PREFIX.length() + extension.length() && lowerCaseFileName.endsWith(extension)) {
                String name = fileName.substring(PREFIX.length(), fileName.length() - extension.length());
                if (NAME.matcher(name).matches()) {
                    return Optional.of(name);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Lists the directories of the {@code PATH} variable. Empty entries are skipped,
     * relative entries are resolved against the working directory like the operating
     * system does, and on Windows entries wrapped in double quotes are accepted.
     *
     * @param environment the process environment
     * @return the directories in search order
     */
    @NonNull
    static List<Path> searchPath(@NonNull Map<String, String> environment) {
        List<Path> directories = new ArrayList<>();
        String path = environmentValue(environment, "PATH");
        if (path == null) {
            return directories;
        }
        for (String entry : path.split(Pattern.quote(File.pathSeparator))) {
            String directory = isWindows() ? stripQuotes(entry) : entry;
            if (directory.isEmpty()) {
                continue;
            }
            try {
                directories.add(Path.of(directory));
            } catch (InvalidPathException invalidPath) {
                // Skip entries the file system rejects.
            }
        }
        return directories;
    }

    private static String stripQuotes(String entry) {
        return entry.length() > 1 && entry.startsWith("\"") && entry.endsWith("\"")
                ? entry.substring(1, entry.length() - 1)
                : entry;
    }

    private static List<String> extensions(Map<String, String> environment) {
        if (!isWindows()) {
            return List.of("");
        }
        List<String> extensions = new ArrayList<>();
        String pathExt = environmentValue(environment, "PATHEXT");
        for (String extension : (pathExt == null ? DEFAULT_PATHEXT : pathExt).split(";")) {
            if (!extension.isBlank()) {
                extensions.add(extension.trim().toLowerCase(Locale.ROOT));
            }
        }
        return extensions.isEmpty() ? List.of("") : extensions;
    }

    /**
     * Looks up an environment variable. Names are case-insensitive on Windows, but the map
     * returned by {@link System#getenv()} is not.
     */
    @Nullable
    private static String environmentValue(Map<String, String> environment, String name) {
        String value = environment.get(name);
        if (value == null && isWindows()) {
            for (Map.Entry<String, String> entry : environment.entrySet()) {
                if (name.equalsIgnoreCase(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return value;
    }
}
