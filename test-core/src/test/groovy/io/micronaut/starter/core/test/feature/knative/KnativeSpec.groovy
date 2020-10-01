package io.micronaut.starter.core.test.feature.knative

import io.micronaut.starter.core.test.feature.testcontainers.book
import io.micronaut.starter.core.test.feature.testcontainers.bookRepository
import io.micronaut.starter.core.test.feature.testcontainers.bookRepositoryTest
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class KnativeSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "knative"
    }

    @Unroll
    void "test knative template with #buildTool"(BuildTool buildTool) {
        when:
        generateProject(Language.JAVA, buildTool, ["knative"])

        String output = null
        if (buildTool.isGradle()) {
            output = executeGradle("test")?.output
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven("compile test")
        }

        then:
        output?.contains("BUILD SUCCESS")
        Files.exists(Paths.get(dir.getPath(), "knativeYaml.yml"))

        where:
        buildTool << BuildToolCombinations.buildTools
    }
}
