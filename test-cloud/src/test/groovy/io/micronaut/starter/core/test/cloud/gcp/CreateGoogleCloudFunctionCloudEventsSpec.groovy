package io.micronaut.starter.core.test.cloud.gcp

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.gcp.GoogleCloudEventsFunction
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Requires
import spock.lang.Retry
import spock.lang.Unroll
import spock.util.environment.Jvm

@Requires({ Jvm.current.isJava11Compatible()})
class CreateGoogleCloudFunctionCloudEventsSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-gcpfunctioncloudevents"
    }

    @Unroll
    void 'create-#applicationType with features google-cloud-function #lang and #build and test framework: #testFramework'(
            ApplicationType applicationType, Language lang, BuildTool build, TestFramework testFramework) {
        given:
        List<String> features = [GoogleCloudEventsFunction.NAME]
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.FUNCTION])
    }
}
