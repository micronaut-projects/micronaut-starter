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

    Path buildGradlePath

    void setup() {
        buildGradlePath = Paths.get(dir.getPath(), "build.gradle")
    }

    @Override
    String getTempDirectoryPrefix() {
        return "jackson"
    }

    @Unroll
    void "test gradle build with serialization-jackson feature"() {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [beanContext.getBeansOfType(SerializationJacksonFeature)[0].getName()])
        String output = executeGradle("compileJava")?.output

        then:
        Files.exists(buildGradlePath)
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "annotationProcessor(\"io.micronaut.serde:micronaut-serde-processor\")")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "implementation(\"io.micronaut.serde:micronaut-serde-jackson\")")

        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == "substitute(module(\"io.micronaut:micronaut-jackson-databind\"))")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.trim()).anyMatch(x -> x == ".using(module(\"io.micronaut.serde:micronaut-serde-jackson:1.0.1\"))")
        output?.contains("BUILD SUCCESS")
    }
}
