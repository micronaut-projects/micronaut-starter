package io.micronaut.starter.generator.awslambda

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateAwsLambdaFunctionGroovyGradleSpockSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "starter-core-test-awslambda-createawslambdafunctiongroovygradlespockspec"
    }

    @Unroll
    void 'create-#applicationType with features #features #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                List<String> features,
                                                                                                                Language lang,
                                                                                                                BuildTool build,
                                                                                                                TestFramework testFramework) {
        given:
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        build == BuildTool.GRADLE ? executeGradleCommand('test') : executeMavenCommand("test")

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        applicationType           | features                                     | lang                    | build              | testFramework
        ApplicationType.FUNCTION  | ['aws-lambda']                               | Language.GROOVY         | BuildTool.GRADLE   | TestFramework.SPOCK
    }
}
