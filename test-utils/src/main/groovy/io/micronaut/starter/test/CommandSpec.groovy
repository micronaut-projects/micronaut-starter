/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.test

import io.micronaut.context.ApplicationContext
import io.micronaut.core.util.functional.ThrowingSupplier
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.io.OutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.NameUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.environment.OperatingSystem

import java.nio.file.Files
import java.time.Duration

abstract class CommandSpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext beanContext

    @Shared
    GradleRunner gradleRunner = GradleRunner.create()

    boolean optimizeWithBuildCache = true

    private boolean gradleBuildCacheConfigurationAdded = false

    abstract String getTempDirectoryPrefix()

    File dir

    void setupSpec() {
        beanContext = ApplicationContext.run(getConfiguration())
    }

    void setup() {
        dir = Files.createTempDirectory(tempDirectoryPrefix).toFile()
    }

    void cleanup() {
        dir.delete()
    }

    Map<String, Object> getConfiguration() {
        return Collections.EMPTY_MAP
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
        command = maybeAddGradleBuildCacheConfiguration(command)
        BuildResult result =
                gradleRunner.withProjectDir(dir)
                        .withArguments(command.split(' '))
                        .forwardOutput()
                        .build()
        return result
    }

    String executeMaven(String command, int timeoutSeconds = 180) {
        if (OperatingSystem.current.isWindows()) {
            command = dir.getAbsolutePath() + "\\" + "mvnw.bat " + command
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

    void generateProject(Language lang,
                         BuildTool buildTool = BuildTool.DEFAULT_OPTION,
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

    void generateProject(Project project,
                         Language lang,
                         BuildTool buildTool = BuildTool.DEFAULT_OPTION,
                         List<String> features = [],
                         ApplicationType applicationType = ApplicationType.DEFAULT,
                         TestFramework testFramework = null) {
        beanContext.getBean(ProjectGenerator).generate(applicationType,
                project,
                new Options(lang, testFramework, buildTool),
                io.micronaut.starter.application.OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    ThrowingSupplier<OutputHandler, IOException> getOutputHandler(ConsoleOutput consoleOutput) {
        return { -> new FileSystemOutputHandler(dir, consoleOutput) }
    }

    private String maybeAddGradleBuildCacheConfiguration(String command) {
        if (!optimizeWithBuildCache) {
            return command
        }
        if (!gradleBuildCacheConfigurationAdded) {
            gradleBuildCacheConfigurationAdded = true
            String cacheUsername = System.getProperty("ge.cache.username")
            String cachePassword = System.getProperty("ge.cache.password")
            def settingsFile = new File(dir, "settings.gradle")
            if (settingsFile.exists()) {
                String push = cacheUsername && cachePassword ? """
                            push = true
                            credentials {
                                username = '$cacheUsername'
                                password = '$cachePassword'
                            }

                    """ : ""
                settingsFile.text += """
                    buildCache {
                        remote(HttpBuildCache) {
                            url = "https://ge.micronaut.io/cache/"
                            $push
                        }
                    }
                    rootProject.name = "micronaut-starter-tests-\${rootProject.name}"
                """
            } else {
                settingsFile = new File(dir, "settings.gradle.kts")
                if (settingsFile.exists()) {
                    String push = cacheUsername && cachePassword ? """
                                isPush = true
                                credentials {
                                    username = "$cacheUsername"
                                    password = "$cachePassword"
                                }
                    """ : ""
                    settingsFile.text += """
                        buildCache {
                            remote<HttpBuildCache> {
                                url = uri("https://ge.micronaut.io/cache/")
                                $push
                            }
                        }
                        rootProject.name = "micronaut-starter-tests-\${rootProject.name}"
                        """
                }
            }
        }
        if (command) {
            "$command --build-cache"
        } else {
            "--build-cache"
        }
    }
}
