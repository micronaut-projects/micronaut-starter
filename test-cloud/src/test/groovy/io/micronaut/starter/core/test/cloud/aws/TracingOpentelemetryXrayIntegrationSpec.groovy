package io.micronaut.starter.core.test.cloud.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryXray
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec

class TracingOpentelemetryXrayIntegrationSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "tracing-opentelemetry-xray-function"
    }

    void 'App with tracing-opentelemetry-xray features builds successfully'() {
        given:
        ApplicationType applicationType = ApplicationType.DEFAULT
        Language lang = Language.JAVA
        BuildTool build = BuildTool.GRADLE_KOTLIN
        TestFramework testFramework = TestFramework.JUNIT

        List<String> features = [OpenTelemetryXray.NAME]
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")
    }
}
