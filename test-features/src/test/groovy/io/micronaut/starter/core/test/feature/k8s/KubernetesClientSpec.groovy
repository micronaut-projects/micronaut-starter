package io.micronaut.starter.core.test.feature.k8s

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.IgnoreIf

class KubernetesClientSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "kubernetesClient"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven #feature with #language"(String feature, Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [feature])
        String output = executeMaven("compile")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [feature, language] << [
                ["kubernetes-client", "kubernetes-reactor-client"],
                Language.values()
        ].combinations()
    }

    void "test #buildTool #feature with #language"(BuildTool buildTool, String feature, Language language) {
        when:
        generateProject(language, buildTool, [feature])
        BuildResult result = executeGradle("compileJava")

        then:
        result?.output?.contains("BUILD SUCCESS")

        where:
        [buildTool, feature, language] << [
                BuildTool.valuesGradle(),
                ["kubernetes-client", "kubernetes-reactor-client"],
                Language.values()
        ].combinations()
    }
}
