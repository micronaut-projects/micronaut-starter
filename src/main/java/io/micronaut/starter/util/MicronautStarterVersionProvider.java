package io.micronaut.starter.util;

import picocli.CommandLine.IVersionProvider;

/**
 * Generates version information. Example usage:
 * <pre>
 * &#064;Command(name = 'command', versionProvider = MicronautCliVersionProvider)
 * class App {
 *     &#064;Option(names = ["-V", "--version"], versionHelp = true, description = 'Display version information and quit.')
 *     boolean isVersionRequested
 * }
 * </pre>
 * Or use picocli's built-in standard usage help option (--help and -h) and version help option (-V and --version).
 * <pre>
 * &#064;Command(name = 'command', mixinStandardHelpOptions = true, versionProvider = MicronautCliVersionProvider)
 * class App {
 *     // ...
 * }
 * </pre>
 *
 * @author Remko Popma
 * @version 1.0
 */
public class MicronautStarterVersionProvider implements IVersionProvider {

    public String[] getVersion() {
        return new String[] {
                "Micronaut Version: " + VersionInfo.getVersion(),
                "JVM Version: " + System.getProperty("java.version")
        };
    }
}
