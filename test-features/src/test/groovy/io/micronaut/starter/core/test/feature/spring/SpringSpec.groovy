package io.micronaut.starter.core.test.feature.spring

import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.feature.spring.SpringBoot
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.RockerWritable
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class SpringSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "spring"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven spring with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [Yaml.NAME, SpringBoot.NAME])

        and:
        // Write a class that requires serialization
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write(
                "src/main/${language.name}/example/micronaut/Book.${language.extension}",
                new RockerWritable(Class.forName("${this.class.package.name}.book${language.name}").template())
        )

        and:
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        language << Language.values() - Language.GROOVY
    }

    void "test #buildTool jax-rs with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, [Yaml.NAME, SpringBoot.NAME])

        and:
        // Write a class that requires serialization
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write(
                "src/main/${language.name}/example/micronaut/Book.${language.extension}",
                new RockerWritable(Class.forName("${this.class.package.name}.book${language.name}").template())
        )

        and:
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.gradleCombinations()
    }
}
