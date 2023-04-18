package io.micronaut.starter.core.test.feature.cdk

import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.Template
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult

class CdkSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "cdk"
    }

    void "test maven cdk feature with #language"(Language language) {
        when:
        generateProject(language, BuildTool.MAVEN, [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])
        String output = executeMaven("compile")

        then:
        output.contains("Building foo 1.0-SNAPSHOT")
        output.contains("Building foo-infra 1.0-SNAPSHOT")
        output.contains("BUILD SUCCESS")

        where:
        language << Language.values()
    }

    void "test #buildTool cdk feature with #language"(Language language, BuildTool buildTool) {
        when:
        generateProject(language, buildTool, [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])
        BuildResult result = executeGradle("classes")

        then:
        result.output.contains("> Task :$Template.DEFAULT_MODULE:classes")
        result.output.contains("> Task :$Cdk.INFRA_MODULE:classes")
        result.output.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << [Language.values(), BuildTool.valuesGradle()].combinations()
    }

}
