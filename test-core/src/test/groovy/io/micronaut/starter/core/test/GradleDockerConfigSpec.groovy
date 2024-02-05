package io.micronaut.starter.core.test

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec

class GradleDockerConfigSpec extends CommandSpec {

    void "basic dockerfile creation"(BuildTool buildTool, Integer javaVersion) {
        when:
        generateProjectForVersion(Language.JAVA, JdkVersion.valueOf(javaVersion), buildTool)

        // dockerfileNative depends on compileJava so we need to exclude it (as if we're on Java 17, we can't compile a java 21 build)
        def result = executeGradle("dockerfile", "dockerfileNative", "-x", "compileJava")

        then:
        result.output.contains("BUILD SUCCESS")
        new File(dir, "build/docker/main/Dockerfile").text.contains("FROM $dockerBaseImage")
        new File(dir, "build/docker/native-main/DockerfileNative").text.contains("FROM ghcr.io/graalvm/native-image-community:$javaVersion-ol9 AS graalvm")

        where:
        [buildTool, javaVersion] << [
                BuildTool.valuesGradle(),
                [17, 21]
        ].combinations()
        dockerBaseImage = javaVersion == 17 ? "eclipse-temurin:17-jre-focal" : "eclipse-temurin:21-jre-jammy"
    }

    void "test dockerfiles can be built"(BuildTool buildTool, String command) {
        when:
        generateProject(Language.JAVA, buildTool)

        def result = executeGradle(command)

        then:
        result.output.contains("BUILD SUCCESS")

        where:
        [buildTool, command] << [
                BuildTool.valuesGradle(),
                ['dockerBuild', 'dockerBuildNative']
        ].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "gradleDockerConfigSpec"
    }
}
