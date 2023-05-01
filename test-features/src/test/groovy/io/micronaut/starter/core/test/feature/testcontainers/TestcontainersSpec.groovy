package io.micronaut.starter.core.test.feature.testcontainers

import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.template.Writable
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.util.VersionInfo
import spock.lang.Ignore
import spock.lang.Retry
import spock.lang.Unroll

// Required so Groovy recognizes these as a class
import io.micronaut.starter.core.test.feature.testcontainers.bookRepository
import io.micronaut.starter.core.test.feature.testcontainers.book
import io.micronaut.starter.core.test.feature.testcontainers.bookRepositoryTest

import java.util.stream.Collectors

@Ignore
@Retry // sometimes CI gets connection failure/reset resolving dependencies from Maven central
class TestcontainersSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "testcontainers"
    }

    @Unroll
    void "test running tests with testcontainers with #buildTool and #driverFeature.getName()"(BuildTool buildTool, DatabaseDriverFeature driverFeature) {
        setup:
        boolean skip = driverFeature.name == "oracle-cloud-atp" && VersionInfo.getJavaVersion() == JdkVersion.JDK_8

        when:
        if (!skip) {
            generateProject(Language.JAVA, buildTool, ["data-jdbc", "testcontainers", driverFeature.getName()])
        }
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write("src/main/java/example/micronaut/BookRepository.java", new RockerWritable(bookRepository.template(driverFeature.getDataDialect())))
        fsoh.write("src/main/java/example/micronaut/Book.java", new RockerWritable(book.template()))
        fsoh.write("src/main/test/example/micronaut/BookRepositoryTest.java", new RockerWritable(bookRepositoryTest.template()))

        String output = null
        if (driverFeature.getName() == "oracle" || driverFeature.getName() == "oracle-cloud-atp") {
            output = "BUILD SUCCESS"
        } else {
            if (buildTool.isGradle()) {
                output = executeGradle("test")?.output
            } else if (buildTool == BuildTool.MAVEN) {
                output = executeMaven("compile test")
            }
        }

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [buildTool, driverFeature] << [
                BuildToolCombinations.buildTools,
                beanContext.streamOfType(DatabaseDriverFeature)
                        .filter({ f ->  !f.embedded() })
                        .collect(Collectors.toList())
        ].combinations()
    }
}
