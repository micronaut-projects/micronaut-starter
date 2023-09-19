package io.micronaut.starter.core.test.feature.micrometer

import io.micronaut.starter.feature.database.DataJdbc
import io.micronaut.starter.feature.micrometer.MicrometerAnnotations
import io.micronaut.starter.feature.validator.MicronautValidationFeature
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class MicrometerSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "micrometer"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven micrometer-cloudwatch with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, ["micrometer-cloudwatch", MicrometerAnnotations.NAME, DataJdbc.NAME, MicronautValidationFeature.NAME])

        and:
        createTemplate(language)

        and:
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test gradle micrometer-cloudwatch with #language"(Language language) {
        when:
        generateProject(language, BuildTool.GRADLE, ["micrometer-cloudwatch", MicrometerAnnotations.NAME, DataJdbc.NAME, MicronautValidationFeature.NAME])

        and:
        createTemplate(language)

        and:
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    private void createTemplate(Language language) {
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write(
                "src/main/${language.name}/example/micronaut/BookController.${language.extension}",
                new RockerWritable(Class.forName("io.micronaut.starter.core.test.feature.micrometer.book${language.name}").template())
        )
    }
}
