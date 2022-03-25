package io.micronaut.starter.core.test.feature.json

import io.micronaut.starter.feature.json.SerializationJsonpFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SerializationJsonpSpec extends CommandSpec {

    Path buildGradlePath

    void setup() {
        buildGradlePath = Paths.get(dir.getPath(), "build.gradle")
    }

    @Override
    String getTempDirectoryPrefix() {
        return "json"
    }

    @Unroll
    void "test gradle build with jsonp feature"() {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, [beanContext.getBeansOfType(SerializationJsonpFeature)[0].getName()])
        String output = executeGradle("compileJava")?.output

        then:
        Files.exists(buildGradlePath)
        Files.readAllLines(buildGradlePath).stream().map(x -> x.strip()).anyMatch(x -> x == "annotationProcessor(\"io.micronaut.serde:micronaut-serde-processor\")")
        Files.readAllLines(buildGradlePath).stream().map(x -> x.strip()).anyMatch(x -> x == "implementation(\"io.micronaut.serde:micronaut-serde-jsonp\")")
        output?.contains("BUILD SUCCESS")
    }
}
