package io.micronaut.starter;

import io.micronaut.starter.util.MicronautStarterVersionProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Mixin that adds help and version options to a command. Example usage:
 * <pre>
 * &#064;Command(name = "command")
 * class App {
 *     &#064;Mixin
 *     HelpOptionsMixin helpOptions // adds help and version options to the command
 *
 *     // ...
 * }
 * </pre>
 *
 * @author Remko Popma
 * @version 1.0
 */
@Command(versionProvider = MicronautStarterVersionProvider.class)
class HelpOptionsMixin {

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message and exit.")
    boolean helpRequested;

    @Option(names = {"-V", "--version"}, versionHelp = true, description = "Print version information and exit.")
    boolean versionRequested;
}
