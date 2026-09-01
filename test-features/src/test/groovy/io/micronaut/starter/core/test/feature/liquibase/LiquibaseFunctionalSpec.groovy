package io.micronaut.starter.core.test.feature.liquibase

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class LiquibaseFunctionalSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "liquibase"
    }

    @Unroll
    void "test #buildTool liquibase integration with #language"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, ["liquibase"])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, language] << [
                // This matrix generates JVM projects; add Python/Pyronaut generation and verification before including Pyronaut.
                BuildTool.valuesGradle().findAll { it != BuildTool.PYRONAUT },
                Language.values().findAll { it != Language.PYTHON }
        ].combinations()
    }
}
