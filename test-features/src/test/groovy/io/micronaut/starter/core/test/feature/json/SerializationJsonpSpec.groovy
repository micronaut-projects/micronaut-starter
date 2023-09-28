package io.micronaut.starter.core.test.feature.json

import io.micronaut.starter.feature.json.SerializationJsonpFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SerializationJsonpSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "json"
    }

    void "test #buildTool build with jsonp feature"(BuildTool buildTool) {
        when:
        generateProject(Language.JAVA, buildTool, [beanContext.getBeansOfType(SerializationJsonpFeature)[0].getName()])
        String output = executeGradle("compileJava")?.output

        then:
        Files.exists(buildGradlePath(buildTool))
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "annotationProcessor(\"io.micronaut.serde:micronaut-serde-processor\")")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "implementation(\"io.micronaut.serde:micronaut-serde-jsonp\")")

        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).noneMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-databind\"))")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).noneMatch(x -> x.startsWith(".using(module(\"jakarta.json.bind:jakarta.json.bind-api"))
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).noneMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-core\"))")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).noneMatch(x -> x.startsWith(".using(module(\"io.micronaut.serde:micronaut-serde-jsonp"))

        output?.contains("BUILD SUCCESS")

        where:
        buildTool << BuildTool.valuesGradle()
    }

    Path buildGradlePath(BuildTool buildTool) {
        String fileName = "build.gradle"
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            fileName += ".kts"
        }
        Paths.get(dir.getPath(), fileName)
    }
}
