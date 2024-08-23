package io.micronaut.starter.core.test.cloud.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class AmazonCloudWatchLoggingFunctionalSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-amazon-cloudwatch-logging"
    }

    @Unroll
    void 'create-app with features amazon-cloudwatch-logging  java and #buildTool and test framework: junit'(BuildTool buildTool) {
        given:
        List<String> features = ['amazon-cloudwatch-logging']
        generateProject(Language.JAVA, buildTool, features, ApplicationType.DEFAULT, TestFramework.JUNIT)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        buildTool << BuildTool.values()
    }
}

