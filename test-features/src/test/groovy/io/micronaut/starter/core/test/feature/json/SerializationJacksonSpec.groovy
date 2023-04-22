package io.micronaut.starter.core.test.feature.json


import io.micronaut.starter.feature.json.SerializationJacksonFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SerializationJacksonSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "jackson"
    }

    @Unroll
    void "test gradle build with serialization-jackson feature"(BuildTool buildTool) {
        when:
        generateProject(Language.JAVA, buildTool, [beanContext.getBeansOfType(SerializationJacksonFeature)[0].getName()])
        String output = executeGradle("compileJava")?.output

        then:
        Files.exists(buildGradlePath(buildTool))
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "annotationProcessor(\"io.micronaut.serde:micronaut-serde-processor\")")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).anyMatch(x -> x == "implementation(\"io.micronaut.serde:micronaut-serde-jackson\")")

        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).noneMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-databind\"))")
        Files.readAllLines(buildGradlePath(buildTool)).stream().map(x -> x.trim()).noneMatch(x -> x.startsWith(".using(module(\"io.micronaut.serde:micronaut-serde-jackson"))
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
