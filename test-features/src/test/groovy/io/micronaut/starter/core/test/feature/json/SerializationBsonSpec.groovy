package io.micronaut.starter.core.test.feature.json

import io.micronaut.starter.feature.json.SerializationBsonFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SerializationBsonSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "bson"
    }

    @Unroll
    void "test gradle build with bson feature"(BuildTool buildTool) {
        when:
        generateProject(Language.JAVA, buildTool, [beanContext.getBeansOfType(SerializationBsonFeature)[0].getName()])
        String output = executeGradle("compileJava")?.output

        then:
        Files.exists(buildGradlePath(buildTool))
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "annotationProcessor(\"io.micronaut.serde:micronaut-serde-processor\")")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "implementation(\"io.micronaut.serde:micronaut-serde-bson\")")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-databind\"))")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x.startsWith(".using(module(\"io.micronaut.serde:micronaut-serde-bson"))
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-core\"))")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x.startsWith(".using(module(\"io.micronaut.serde:micronaut-serde-bson"))
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
