package io.micronaut.starter.core.test.cloud.oracecloud

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Retry
import spock.lang.Unroll

@Retry // can fail on CI due to port binding race condition, so retry
class CreateOracleFunctionSpec extends CommandSpec{
    @Override
    String getTempDirectoryPrefix() {
        "test-oraclefunction"
    }

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
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT], [Language.GROOVY, Language.JAVA, Language.KOTLIN], [BuildTool.GRADLE])
    }

    void 'default application with features oracle-function, #serializationFeature, #lang and #build and test framework: #testFramework'(
            Language lang,
            String serializationFeature,
            BuildTool build,
            TestFramework testFramework
    ) {
        given:
        List<String> features = ['oracle-function'] + serializationFeature
        generateProject(lang, build, features, ApplicationType.DEFAULT, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, serializationFeature, build, testFramework] << [
                Language.values(),
                ['serialization-jackson', 'serialization-bson', 'serialization-jsonp'],
                BuildTool.values(),
                TestFramework.values()
        ].combinations()
    }
}
