package io.micronaut.starter.core.test.cloud.gcp

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Requires
import spock.lang.Unroll
import spock.util.environment.Jvm

@Requires({Jvm.current.isJava11Compatible()})
class CreateGoogleCloudFunctionSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "test-gcpfunction"
    }

    @Unroll
    void 'create-#applicationType with features google-cloud-function #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                           Language lang,
                                                                                                                           BuildTool build,
                                                                                                                           TestFramework testFramework) {
        given:
        List<String> features = ['google-cloud-function']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT])
    }
}
