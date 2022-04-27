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

    Path buildGradlePath

    void setup() {
        buildGradlePath = Paths.get(dir.getPath(), "build.gradle")
    }

    @Override
    String getTempDirectoryPrefix() {
        return "bson"
    }

    @Unroll
    void "test gradle build with bson feature"() {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [beanContext.getBeansOfType(SerializationBsonFeature)[0].getName()])
        String output = executeGradle("compileJava")?.output

        then:
        Files.exists(buildGradlePath)
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "annotationProcessor(\"io.micronaut.serde:micronaut-serde-processor\")")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "implementation(\"io.micronaut.serde:micronaut-serde-bson\")")

        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-databind\"))")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == ".using(module(\"jakarta.json.bind:jakarta.json.bind-api:2.0.0\"))")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-core\"))")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == ".using(module(\"io.micronaut.serde:micronaut-serde-bson:1.0.1\"))")
        output?.contains("BUILD SUCCESS")
    }
}
