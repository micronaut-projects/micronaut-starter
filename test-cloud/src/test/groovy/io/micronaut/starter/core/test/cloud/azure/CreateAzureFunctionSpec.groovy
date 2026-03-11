package io.micronaut.starter.core.test.cloud.azure

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.azure.AbstractAzureFunction
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import io.micronaut.starter.test.TestFrameworkCombinations

class CreateAzureFunctionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-azure-function"
    }

    void 'create-#applicationType with features azure-function #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                Language lang,
                                                                                                                BuildTool build,
                                                                                                                TestFramework testFramework) {
        given:
        List<String> features = [AbstractAzureFunction.NAME]
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] <<
                ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT, ApplicationType.FUNCTION], Language.values() as List<Language>, BuildToolCombinations.buildTools)
    }

    void 'default application with features azure-function, #serializationFeature, #lang and #build and test framework: #testFramework'(
            Language lang,
            String serializationFeature,
            BuildTool build,
            TestFramework testFramework
    ) {
        given:
        List<String> features = [AbstractAzureFunction.NAME] + serializationFeature
        generateProject(lang, build, features, ApplicationType.DEFAULT, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, build, serializationFeature,testFramework] << [
                Language.values(),
                BuildToolCombinations.buildTools,
                ['serialization-jackson', 'serialization-bson', 'serialization-jsonp'],
                TestFrameworkCombinations.values()
        ].combinations().findAll {
            LanguageBuildCombinations.SKIP_KOTLIN_MAVEN.apply(it)
        }
    }
}
