package io.micronaut.starter.test

import io.micronaut.context.BeanContext
import io.micronaut.core.util.functional.ThrowingSupplier
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.io.OutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.NameUtils
import org.apache.maven.shared.invoker.DefaultInvocationRequest
import org.apache.maven.shared.invoker.DefaultInvoker
import org.apache.maven.shared.invoker.InvocationOutputHandler
import org.apache.maven.shared.invoker.InvocationRequest
import org.apache.maven.shared.invoker.InvocationResult
import org.apache.maven.shared.invoker.Invoker
import org.apache.maven.shared.invoker.PrintStreamHandler
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.environment.OperatingSystem
import java.nio.file.Files
import java.time.Duration

abstract class CommandSpec extends Specification {

    static final Invoker mavenInvoker = new DefaultInvoker()

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Shared
    GradleRunner gradleRunner = GradleRunner.create()

    abstract String getTempDirectoryPrefix()

    File dir

    void setup() {
        dir = Files.createTempDirectory(tempDirectoryPrefix).toFile()
    }

    void cleanup() {
        dir.delete()
    }

    String executeBuild(BuildTool buildTool, String command) {
        String output = null
        if (buildTool.isGradle()) {
            output = executeGradle(command).getOutput()
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven(command)
        }
        return output
    }

    BuildResult executeGradle(String command) {
        BuildResult result =
                gradleRunner.withProjectDir(dir)
                        .withArguments(command.split(' '))
                        .build()
        return result
    }

    String executeMaven(String command, int timeoutSeconds = 180) {
        if (OperatingSystem.current.isWindows()) {
            command = dir.getAbsolutePath()+"\\"+"mvnw.bat " + command
        } else {
            command = "./mvnw " + command
        }
        String[] args = command.split(" ")
        ProcessBuilder pb = new ProcessBuilder(args)
        Map<String, String> env = pb.environment()
        env["JAVA_HOME"] = System.getenv("JAVA_HOME")

        Process process = pb.directory(dir).start()

        StringBuilder output = new StringBuilder()
        def thread = process.consumeProcessOutputStream(output)
        try {
            thread.join(Duration.ofSeconds(timeoutSeconds).toMillis())
        } catch (InterruptedException e) {
        }

        try {
            return output.toString()
        } finally {
            process.destroy()
        }
    }

    String invokeMaven(String goals, Map properties = null) {
        if (!mavenInvoker.mavenHome) {
            String mavenHome = executeMaven("--version")
                    .split(System.getProperty("line.separator"))
                    .find {it.startsWith("Maven home: ")}
                    .replace("Maven home: ", "")

            mavenInvoker.setMavenHome(new File(mavenHome))
        }
        InvocationRequest request = new DefaultInvocationRequest()
        request.setBaseDirectory(dir)
        request.setBatchMode(true)
        request.setGoals(goals.split(" ").toList())
        if (properties) {
            request.setProperties(properties as Properties)
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        InvocationOutputHandler outputHandler = new PrintStreamHandler(new PrintStream(baos, true), true)
        request.setOutputHandler(outputHandler)
        InvocationResult result = mavenInvoker.execute(request)
        return baos.toString()
    }

    void generateProject(Language lang,
                         BuildTool buildTool = BuildTool.GRADLE,
                         List<String> features = [],
                         ApplicationType applicationType = ApplicationType.DEFAULT,
                         TestFramework testFramework = null) {
        beanContext.getBean(ProjectGenerator).generate(applicationType,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, testFramework, buildTool),
                io.micronaut.starter.application.OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }


    ThrowingSupplier<OutputHandler, IOException> getOutputHandler(ConsoleOutput consoleOutput) {
        return { -> new FileSystemOutputHandler(dir, consoleOutput)}
    }

}
