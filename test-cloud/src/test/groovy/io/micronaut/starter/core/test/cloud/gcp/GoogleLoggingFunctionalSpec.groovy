package io.micronaut.starter.core.test.cloud.gcp;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.test.ApplicationTypeCombinations;
import io.micronaut.starter.test.CommandSpec;
import spock.lang.Unroll;

import java.util.List;

class GoogleLoggingFunctionalSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-gcp-logging"
    }

    @Unroll
    void 'create-app with features gcp-logging  java and #buildTool and test framework: junit'(BuildTool buildTool) {
        given:
        List<String> features = ['gcp-logging']
        generateProject(Language.JAVA, buildTool, features, ApplicationType.DEFAULT, TestFramework.JUNIT)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        buildTool << BuildTool.values()
    }
}

