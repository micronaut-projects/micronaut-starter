package io.micronaut.starter.core.test.feature.cdk

import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.Template
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.BuildResult

class CdkSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "cdk"
    }

    void "test #buildTool cdk feature with #language"(Language language, BuildTool buildTool) {
        when:
        generateProject(language, buildTool, [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])

        BuildResult result
        String output
        if (buildTool.isGradle()) {
            result = executeGradle("classes")
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven("compile")
        }

        then:
        if (buildTool.isGradle()) {
            assert result
            assert result.output.contains("> Task :$Template.DEFAULT_MODULE:classes")
            assert result.output.contains("> Task :$Cdk.INFRA_MODULE:classes")
            assert result.output.contains("BUILD SUCCESS")
        } else if (buildTool == BuildTool.MAVEN) {
            assert output
            assert output.contains("Building foo 1.0-SNAPSHOT")
            assert output.contains("Building foo-infra 1.0-SNAPSHOT")
            assert output.contains("BUILD SUCCESS")
        }

        where:
        [language, buildTool] << LanguageBuildCombinations.combinations()
    }
}
