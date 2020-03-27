package io.micronaut.starter;

import picocli.CommandLine.Option;

/**
 * Mixin that adds help, version and other common options to a command. Example usage:
 * <pre>
 * &#064;Command(name = "command")
 * class App {
 *     &#064;Mixin
 *     CommonOptionsMixin commonOptions // adds common options to the command
 *
 *     // ...
 * }
 * </pre>
 *
 * @author Remko Popma
 * @version 1.0
 */
public class CommonOptionsMixin extends HelpOptionsMixin {

    @Option(names = {"-x", "--stacktrace"}, defaultValue = "false", description = "Show full stack trace when exceptions occur.")
    public boolean showStacktrace;

    @Option(names = {"-v", "--verbose"}, defaultValue = "false", description = "Create verbose output.")
    public boolean verbose;
}
