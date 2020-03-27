package io.micronaut.starter;

import io.micronaut.starter.util.MicronautVersionProvider;
import picocli.CommandLine;
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
@CommandLine.Command(mixinStandardHelpOptions = true, versionProvider = MicronautVersionProvider.class)
public class CommonOptionsMixin {

    @Option(names = {"-x", "--stacktrace"}, defaultValue = "false", description = "Show full stack trace when exceptions occur.")
    public boolean showStacktrace;

    @Option(names = {"-v", "--verbose"}, defaultValue = "false", description = "Create verbose output.")
    public boolean verbose;
}
