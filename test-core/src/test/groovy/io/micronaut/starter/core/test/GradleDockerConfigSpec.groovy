package io.micronaut.starter.core.test

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Requires

class GradleDockerConfigSpec extends CommandSpec {

    @Requires({ javaVersion == 17 })
    void "Java 17 basic dockerfile creation"(BuildTool buildTool, Integer javaVersion) {
        when:
        generateProjectForVersion(Language.JAVA, JdkVersion.valueOf(javaVersion), buildTool)
        BuildResult result = executeGradle("dockerfile", "dockerfileNative")

        then:
        result.output.contains("BUILD SUCCESS")
        new File(dir, "build/docker/main/Dockerfile").text.contains("FROM $dockerBaseImage")
        new File(dir, "build/docker/native-main/DockerfileNative").text.contains("FROM ghcr.io/graalvm/native-image-community:$javaVersion-ol9 AS graalvm")

        where:
        [buildTool, javaVersion] << [BuildTool.valuesGradle(), [17]].combinations()
        dockerBaseImage = "eclipse-temurin:17-jre"
    }

    @Requires({ javaVersion == 21 })
    void "Java 21 basic dockerfile creation"(BuildTool buildTool, Integer javaVersion) {
        when:
        generateProjectForVersion(Language.JAVA, JdkVersion.valueOf(javaVersion), buildTool)
        BuildResult result = executeGradle("dockerfile", "dockerfileNative")

        then:
        result.output.contains("BUILD SUCCESS")
        new File(dir, "build/docker/main/Dockerfile").text.contains("FROM $dockerBaseImage")
        new File(dir, "build/docker/native-main/DockerfileNative").text.contains("FROM ghcr.io/graalvm/native-image-community:$javaVersion-ol9 AS graalvm")

        where:
        [buildTool, javaVersion] << [BuildTool.valuesGradle(), [21]].combinations()
        dockerBaseImage = "eclipse-temurin:21-jre-jammy"
    }

    void "test dockerfiles can be built"(BuildTool buildTool, String command) {
        when:
        generateProject(Language.JAVA, buildTool)
        BuildResult result = executeGradle(command)

        then:
        result.output.contains("BUILD SUCCESS")

        where:
        [buildTool, command] << [BuildTool.valuesGradle(), ['dockerBuild', 'dockerBuildNative']].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "gradleDockerConfigSpec"
    }
}
