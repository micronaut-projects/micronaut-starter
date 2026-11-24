package io.micronaut.starter.cli

import picocli.CommandLine
import spock.lang.Requires
import spock.lang.Specification
import spock.lang.TempDir
import spock.util.concurrent.PollingConditions
import spock.util.environment.OperatingSystem

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermissions
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class ExternalCommandSpec extends Specification {

    @TempDir
    Path bin

    CommandLine root = MicronautStarter.createCommandLine()

    void "a built-in command is never dispatched to an external executable"() {
        given:
        executable("mn-create-app")

        expect:
        ExternalCommand.find(root, ["create-app", "demo"] as String[], environment()).isEmpty()
    }

    void "a registered built-in alias is never dispatched to an external executable"() {
        given:
        root.addSubcommand("ca", root.getSubcommands().get("create-app"))
        executable("mn-ca")

        expect:
        ExternalCommand.find(root, ["ca", "demo"] as String[], environment()).isEmpty()
    }

    void "names that are not plain lower-case words are never looked up"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, [name] as String[], environment()).isEmpty()

        where:
        name << ["../hello", "hello.sh", "Hello", "-hello", "hello/x", "", ".", "hello world"]
    }

    void "an unknown name without an executable is left to picocli"() {
        expect:
        ExternalCommand.find(root, ["nothing-here"] as String[], environment()).isEmpty()
        ExternalCommand.locate("nothing-here", environment()).isEmpty()
    }

    void "the executable on PATH is found with the remaining arguments"() {
        given:
        Path hello = executable("mn-hello")

        when:
        Optional<ExternalCommand> command = ExternalCommand.find(root, ["hello", "--flag", "value"] as String[], environment())

        then:
        command.present
        command.get().name == "hello"
        command.get().executable == hello
        command.get().arguments == ["--flag", "value"]
    }

    void "root options and the option separator can precede an external command"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, ["--verbose", "hello", "one"] as String[], environment()).get().arguments == ["one"]
        ExternalCommand.find(root, ["-x", "-v", "hello", "one"] as String[], environment()).get().arguments == ["one"]
        ExternalCommand.find(root, ["--", "hello", "two"] as String[], environment()).get().arguments == ["two"]
    }

    void "boolean root options accept attached values before an external command"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, ["--verbose=false", "hello"] as String[], environment()).present
        ExternalCommand.find(root, ["--verbose=bogus", "hello"] as String[], environment()).isEmpty()
    }

    void "unknown and clustered options before an external command are left to picocli"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, [option, "hello"] as String[], environment()).isEmpty()

        where:
        option << ["--unknown", "-xv", "-vx", "--verbos", "-"]
    }

    void "root options that take a value are left to picocli"() {
        given:
        executable("mn-hello")
        CommandLine valueRoot = rootWithValueOption()

        expect:
        ExternalCommand.find(valueRoot, ["-q", "hello"] as String[], environment()).present
        // the value of --name looks like the name of an external command
        ExternalCommand.find(valueRoot, ["--name", "hello", "one"] as String[], environment()).isEmpty()
        ExternalCommand.find(valueRoot, ["--name=hello", "hello"] as String[], environment()).isEmpty()
    }

    void "an option separator without a command is left to picocli"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, ["--"] as String[], environment()).isEmpty()
        ExternalCommand.find(root, ["--verbose"] as String[], environment()).isEmpty()
        ExternalCommand.find(root, ["--verbose", "create-app"] as String[], environment()).isEmpty()
    }

    void "help and version retain precedence over external commands"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, ["hello"] as String[], environment()).present
        ExternalCommand.find(root, [option, "hello"] as String[], environment()).isEmpty()
        ExternalCommand.find(root, ["--verbose", option, "hello"] as String[], environment()).isEmpty()

        where:
        option << ["--help", "-h", "--version", "-V"]
    }

    void "options after the name belong to the external command"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.find(root, ["hello", "--help", "-x", "--"] as String[], environment()).get().arguments == ["--help", "-x", "--"]
    }

    void "empty PATH entries are skipped"() {
        expect:
        ExternalCommand.searchPath([PATH: ["one", "", "two", ""].join(File.pathSeparator)]) == [Path.of("one"), Path.of("two")]
        ExternalCommand.searchPath([PATH: File.pathSeparator]).isEmpty()
        ExternalCommand.searchPath([:]).isEmpty()
        ExternalCommand.locate("hello", [PATH: ""]).isEmpty()
        ExternalCommand.locate("hello", [PATH: File.pathSeparator + File.pathSeparator]).isEmpty()
    }

    @Requires({ !OperatingSystem.current.windows })
    void "files that are not executable are ignored"() {
        given:
        Path plain = bin.resolve("mn-plain")
        Files.writeString(plain, "not executable")
        Files.setPosixFilePermissions(plain, PosixFilePermissions.fromString("rw-r--r--"))

        expect:
        ExternalCommand.locate("plain", environment()).isEmpty()
        ExternalCommand.available(environment()).isEmpty()
    }

    void "the first matching PATH entry wins"() {
        given:
        Path second = Files.createDirectories(bin.resolve("second"))
        Path first = executable("mn-hello")
        Path later = executable(second, "mn-hello")

        expect:
        ExternalCommand.locate("hello", environment(bin, second)).get() == first
        ExternalCommand.locate("hello", environment(second, bin)).get() == later
    }

    @Requires({ !OperatingSystem.current.windows })
    void "relative PATH entries are resolved from the working directory"() {
        given:
        executable("mn-relative")
        Path relativeBin = Path.of("").toAbsolutePath().relativize(bin.toAbsolutePath())

        expect:
        ExternalCommand.locate("relative", [PATH: relativeBin.toString()]).present
    }

    @Requires({ !OperatingSystem.current.windows })
    void "environment variable names are case-sensitive outside Windows"() {
        given:
        executable("mn-hello")

        expect:
        ExternalCommand.locate("hello", [Path: bin.toString()]).isEmpty()
    }

    @Requires({ OperatingSystem.current.windows })
    void "a Windows PATH may use any case for the variable names and quote its entries"() {
        given:
        Path hello = executable("mn-hello")

        expect:
        ExternalCommand.locate("hello", [Path: '"' + bin + '"', PathExt: ".CMD"]).get() == hello
        ExternalCommand.locate("hello", [path: bin.toString(), pathext: ".CMD"]).get() == hello
    }

    void "help lists the external commands found on PATH"() {
        given:
        executable("mn-hello")
        executable("mn-world")
        executable("mn-Upper")
        executable("mn-Not.Valid")

        expect:
        ExternalCommand.available(environment()) == ["hello", "world"]
        ExternalCommand.available([PATH: bin.resolve("missing").toString()]).isEmpty()
    }

    @Requires({ OperatingSystem.current.windows })
    void "help ignores files with a PATHEXT suffix that do not have the mn prefix"() {
        given:
        executable("otherhello")

        expect:
        ExternalCommand.available(environment()).isEmpty()
    }

    void "mn --help lists the external commands, but not those hidden by a built-in command"() {
        given:
        executable("mn-hello")
        executable("mn-create-app")
        CommandLine commandLine = MicronautStarter.createCommandLine(environment())

        expect:
        externalCommandsSection(commandLine) == ["External commands: (mn-<name> executables on PATH)", "  hello"]
    }

    void "mn --help has no external commands section without external commands"() {
        expect:
        externalCommandsSection(MicronautStarter.createCommandLine(environment())).isEmpty()
    }

    void "PATH is scanned when the help is rendered, not when the command line is created"() {
        given:
        CommandLine commandLine = MicronautStarter.createCommandLine(environment())
        executable("mn-late")

        expect:
        externalCommandsSection(commandLine) == ["External commands: (mn-<name> executables on PATH)", "  late"]
    }

    void "the help of a built-in command does not list external commands"() {
        given:
        executable("mn-hello")
        CommandLine commandLine = MicronautStarter.createCommandLine(environment())

        expect:
        externalCommandsSection(commandLine.getSubcommands().get("create-app")).isEmpty()
    }

    @Requires({ !OperatingSystem.current.windows })
    void "mn runs the external executable, passes its arguments through and returns its exit code"() {
        given:
        Path marker = bin.resolve("marker.txt")
        script("mn-hello", "#!/bin/sh\nprintf '%s|' \"\$@\" > \"${marker}\"\nexit 3\n")

        when:
        int exitCode = MicronautStarter.execute(["hello", "one", "--two", "three four"] as String[], environment())

        then:
        exitCode == 3
        Files.readString(marker) == "one|--two|three four|"
    }

    @Requires({ !OperatingSystem.current.windows })
    void "root options before the external command are consumed by mn"() {
        given:
        Path marker = bin.resolve("marker.txt")
        script("mn-hello", "#!/bin/sh\nprintf '%s|' \"\$@\" > \"${marker}\"\n")

        when:
        int exitCode = MicronautStarter.execute(["-x", "--verbose", "hello", "-v", "one"] as String[], environment())

        then:
        exitCode == 0
        Files.readString(marker) == "-v|one|"
    }

    @Requires({ !OperatingSystem.current.windows })
    void "an external command that cannot be started exits with 127 and a message"() {
        given:
        script("mn-broken", "#!/nonexistent/interpreter\n")

        when:
        Map result = execute(["broken"], environment())

        then:
        result.exitCode == ExternalCommand.CANNOT_RUN
        result.stderr.contains("mn: cannot run ")
    }

    @Requires({ !OperatingSystem.current.windows })
    void "interrupting an external command destroys its child and restores the interrupt"() {
        given:
        Path pidFile = bin.resolve("child.pid")
        script("mn-sleep", "#!/bin/sh\nprintf '%s' \"\$\$\" > \"${pidFile}\"\nexec sleep 30\n")
        ExternalCommand command = ExternalCommand.find(root, ["sleep"] as String[], environment()).get()
        AtomicInteger exitCode = new AtomicInteger()
        AtomicBoolean interrupted = new AtomicBoolean()
        Thread worker = runInBackground(command, exitCode, interrupted)
        long childPid = awaitPid(pidFile)

        when:
        worker.interrupt()
        worker.join(5000)

        then:
        !worker.isAlive()
        exitCode.get() == ExternalCommand.CANNOT_RUN
        interrupted.get()
        new PollingConditions(timeout: 5, initialDelay: 0.1, delay: 0.1).eventually {
            assert !isAlive(childPid)
        }

        cleanup:
        if (worker?.isAlive()) {
            worker.interrupt()
            worker.join(5000)
        }
        if (childPid > 0) {
            ProcessHandle.of(childPid).ifPresent { handle -> handle.destroyForcibly() }
        }
    }

    @Requires({ !OperatingSystem.current.windows })
    void "interrupting an external command also destroys the processes it started"() {
        given:
        Path pidFile = bin.resolve("grandchild.pid")
        // The script does not exec, so sleep is a child of the script and not the command itself.
        script("mn-wrapper", "#!/bin/sh\nsleep 30 &\nprintf '%s' \"\$!\" > \"${pidFile}\"\nwait\n")
        ExternalCommand command = ExternalCommand.find(root, ["wrapper"] as String[], environment()).get()
        Thread worker = runInBackground(command, new AtomicInteger(), new AtomicBoolean())
        long grandchildPid = awaitPid(pidFile)

        when:
        worker.interrupt()
        worker.join(5000)

        then:
        !worker.isAlive()
        new PollingConditions(timeout: 5, initialDelay: 0.1, delay: 0.1).eventually {
            assert !isAlive(grandchildPid)
        }

        cleanup:
        if (worker?.isAlive()) {
            worker.interrupt()
            worker.join(5000)
        }
        if (grandchildPid > 0) {
            ProcessHandle.of(grandchildPid).ifPresent { handle -> handle.destroyForcibly() }
        }
    }

    @Requires({ OperatingSystem.current.windows })
    void "mn runs a Windows batch external command with quoted arguments"() {
        given:
        Path marker = bin.resolve("marker.txt")
        script("mn-hello.cmd", "@echo off\r\n>\"${marker}\" echo arg1=%~1 arg2=%~2\r\nexit /b 7\r\n")

        when:
        int exitCode = MicronautStarter.execute(["hello", "three four", ""] as String[], environment())

        then:
        exitCode == 7
        Files.readString(marker).trim() == "arg1=three four arg2="
    }

    @Requires({ OperatingSystem.current.windows })
    void "mn runs a Windows batch external command from a PATH directory with spaces"() {
        given:
        Path spacedBin = bin.resolve("path with spaces")
        Files.createDirectories(spacedBin)
        Path marker = spacedBin.resolve("marker.txt")
        script(spacedBin, "mn-hello.cmd", "@echo off\r\n>\"${marker}\" echo arg1=%~1 arg2=[%~2] arg3=[%~3]\r\nexit /b 7\r\n")

        when:
        int exitCode = MicronautStarter.execute(["hello", "three four", "", "tail\\"] as String[], environment(spacedBin))

        then:
        exitCode == 7
        Files.readString(marker).trim() == "arg1=three four arg2=[] arg3=[tail\\]"
    }

    @Requires({ OperatingSystem.current.windows })
    void "mn runs a Windows batch external command from a PATH directory named #directory"() {
        given:
        Path special = Files.createDirectories(bin.resolve(directory))
        Path marker = special.resolve("marker.txt")
        script(special, "mn-hello.cmd", "@echo off\r\n>\"${marker}\" echo arg1=%~1\r\nexit /b 7\r\n")

        when:
        Map result = execute(["hello", "three four"], environment(special))

        then:
        result.exitCode == 7
        Files.readString(marker).trim() == "arg1=three four"

        where:
        directory << ["bin (x86)", "r&d", "caret^dir"]
    }

    @Requires({ OperatingSystem.current.windows })
    void "mn refuses a Windows batch external command whose path contains #directory"() {
        given:
        Path special = Files.createDirectories(bin.resolve(directory))
        Path marker = special.resolve("marker.txt")
        script(special, "mn-hello.cmd", "@echo off\r\n>\"${marker}\" echo invoked\r\nexit /b 7\r\n")

        when:
        Map result = execute(["hello"], environment(special))

        then:
        result.exitCode == ExternalCommand.CANNOT_RUN
        result.stderr.contains("the path of the batch file contains")
        !Files.exists(marker)

        where:
        directory << ["100%", "bang!"]
    }

    @Requires({ OperatingSystem.current.windows })
    void "mn refuses shell metacharacters for a Windows batch external command"() {
        given:
        Path marker = bin.resolve("marker.txt")
        script("mn-hello.cmd", "@echo off\r\n>\"${marker}\" echo invoked\r\nexit /b 7\r\n")

        when:
        Map result = execute(["hello", argument], environment())

        then:
        result.exitCode == ExternalCommand.CANNOT_RUN
        result.stderr.contains("argument 1 contains")
        !Files.exists(marker)

        where:
        argument << ["a&echo INJECTED", "%PATH%", "a|echo INJECTED", "a^b", "a\"b", "a(b", "a)b", "a!b"]
    }

    @Requires({ OperatingSystem.current.windows })
    void "mn runs a Windows exe external command directly"() {
        given:
        Files.copy(Path.of(System.getenv("ComSpec")), bin.resolve("mn-shell.exe"))

        expect:
        MicronautStarter.execute((["shell"] + arguments) as String[], environment()) == exitCode

        where:
        arguments                     | exitCode
        ["/c", "exit 5"]              | 5
        ["/c", "exit 6 & rem (x86)"]  | 6
    }

    void "a batch file path or argument is only refused when cmd.exe would interpret it"() {
        given:
        Path batchFile = Path.of(directory, "mn-hello.cmd")

        expect:
        ExternalCommand.batchRefusal(batchFile, ["--name", "three four", "", "tail\\", "--flag=value", "a,b;c", "C:\\x\\", "'single'"]).isEmpty()

        where:
        directory << ["bin", "path with spaces", "bin (x86)", "r&d", "caret^dir", "it's"]
    }

    void "a batch file path containing #directory is refused"() {
        expect:
        ExternalCommand.batchRefusal(Path.of(directory, "mn-hello.cmd"), ["ok"]).get().startsWith("the path of the batch file contains ")

        where:
        directory << ["100%", "bang!"]
    }

    void "a batch file argument containing the metacharacter #character is refused"() {
        expect:
        ExternalCommand.batchRefusal(Path.of("mn-hello.cmd"), ["ok", ("a" + character + "b").toString()]).get() ==
                "argument 2 contains the command-shell metacharacter '" + character + "'"

        where:
        character << ['"', "%", "&", "(", ")", "<", ">", "^", "|", "!"]
    }

    void "a batch file argument containing a #name control character is refused"() {
        expect:
        ExternalCommand.batchRefusal(Path.of("mn-hello.cmd"), [("a" + character + "b").toString()]).get().startsWith("argument 1 contains a control character (U+")

        where:
        name      | character
        "tab"     | "\t"
        "newline" | "\n"
        "return"  | "\r"
        "SOH"     | "\u0001"
    }

    void "an unknown command without an executable still fails the picocli way"() {
        expect:
        MicronautStarter.execute(["definitely-unknown"] as String[], environment()) != 0
    }

    @Requires({ !OperatingSystem.current.windows })
    void "a built-in command still runs even when a same-named executable is on PATH"() {
        given:
        Path marker = bin.resolve("marker.txt")
        script("mn-create-app", "#!/bin/sh\necho hijacked > \"${marker}\"\nexit 3\n")

        when:
        int exitCode = MicronautStarter.execute(["create-app", "--help"] as String[], environment())

        then:
        exitCode == 0
        !Files.exists(marker)
    }

    // Built programmatically: ReflectiveValidatorSpec rejects annotated options without @ReflectiveAccess.
    private static CommandLine rootWithValueOption() {
        CommandLine.Model.CommandSpec spec = CommandLine.Model.CommandSpec.create().name("root")
        spec.addOption(CommandLine.Model.OptionSpec.builder("-q").type(boolean).arity("0").build())
        spec.addOption(CommandLine.Model.OptionSpec.builder("--name").type(String).arity("1").build())
        new CommandLine(spec)
    }

    private static List<String> externalCommandsSection(CommandLine commandLine) {
        List<String> lines = commandLine.getUsageMessage(CommandLine.Help.Ansi.OFF).readLines()
        int heading = lines.findIndexOf { it.startsWith("External commands:") }
        heading < 0 ? [] : lines.drop(heading).findAll { !it.isBlank() }
    }

    private Map execute(List<String> args, Map<String, String> environment) {
        PrintStream original = System.err
        ByteArrayOutputStream captured = new ByteArrayOutputStream()
        System.setErr(new PrintStream(captured, true))
        try {
            int exitCode = MicronautStarter.execute(args as String[], environment)
            [exitCode: exitCode, stderr: captured.toString()]
        } finally {
            System.setErr(original)
        }
    }

    private Thread runInBackground(ExternalCommand command, AtomicInteger exitCode, AtomicBoolean interrupted) {
        Thread.start {
            exitCode.set(command.run())
            interrupted.set(Thread.currentThread().isInterrupted())
        }
    }

    private long awaitPid(Path pidFile) {
        new PollingConditions(timeout: 10, initialDelay: 0.1, delay: 0.1).eventually {
            assert Files.exists(pidFile)
            assert !Files.readString(pidFile).isBlank()
        }
        Long.parseLong(Files.readString(pidFile))
    }

    private static boolean isAlive(long pid) {
        ProcessHandle.of(pid).map { handle -> handle.isAlive() }.orElse(false)
    }

    private Map<String, String> environment() {
        environment(bin)
    }

    private Map<String, String> environment(Path... directories) {
        [PATH: directories.collect { it.toString() }.join(File.pathSeparator), PATHEXT: ".EXE;.CMD;.BAT"]
    }

    private Path executable(String name) {
        executable(bin, name)
    }

    private Path executable(Path directory, String name) {
        String fileName = OperatingSystem.current.windows ? name + ".cmd" : name
        script(directory, fileName, OperatingSystem.current.windows ? "@echo off\r\nexit /b 0\r\n" : "#!/bin/sh\nexit 0\n")
    }

    private Path script(String fileName, String content) {
        script(bin, fileName, content)
    }

    private Path script(Path directory, String fileName, String content) {
        Path file = directory.resolve(fileName)
        Files.writeString(file, content)
        if (!OperatingSystem.current.windows) {
            Files.setPosixFilePermissions(file, PosixFilePermissions.fromString("rwxr-xr-x"))
        }
        file
    }
}
