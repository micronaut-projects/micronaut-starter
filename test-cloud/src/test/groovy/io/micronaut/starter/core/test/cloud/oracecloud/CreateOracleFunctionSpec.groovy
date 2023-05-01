package io.micronaut.starter.core.test.cloud.oracecloud

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Ignore
import spock.lang.Retry

@Ignore("[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile (default-compile) on project foo: Fatal error compiling: java.lang.NoSuchMethodError: Micronaut method io.micronaut.context.AbstractInitializableBeanDefinition.getEvaluatedExpressionValueForMethodArgument(int,int) not found. Most likely reason for this issue is that you are running a newer version of Micronaut with code compiled against an older version. Please recompile the offending classes -> [Help 1]")
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
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT], Language.values() as List<Language>, BuildTool.valuesGradle())
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
                BuildToolCombinations.buildTools,
                TestFramework.values()
        ].combinations()
    }
}
