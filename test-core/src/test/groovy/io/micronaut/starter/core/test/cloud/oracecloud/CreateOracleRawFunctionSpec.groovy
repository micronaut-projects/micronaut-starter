package io.micronaut.starter.core.test.cloud.oracecloud

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class CreateOracleRawFunctionSpec extends CommandSpec{
    @Override
    String getTempDirectoryPrefix() {
        "test-raw-oraclefunction"
    }

    @Unroll
    void 'create-#applicationType with features oracle-function #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                     Language lang,
                                                                                                                     BuildTool build,
                                                                                                                     TestFramework testFramework) {
        given:
        List<String> features = ['oracle-function']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "testClasses")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.FUNCTION], [Language.GROOVY, Language.JAVA, Language.KOTLIN], [BuildTool.GRADLE])
    }
}
