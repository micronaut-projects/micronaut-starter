package io.micronaut.starter.core.test.aws

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class CreateAwsLambdaDynamoDbSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-awslambda-dynamodb"
    }

    @Unroll
    void 'create-#applicationType with features aws-lambda and dynamodb #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                             Language lang,
                                                                                                                             BuildTool build,
                                                                                                                             TestFramework testFramework) {
        given:
        List<String> features = ['aws-lambda','dynamodb']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT, ApplicationType.FUNCTION])
                .stream()
                .filter(l -> !(l[1] == Language.KOTLIN && l[2] == BuildTool.MAVEN) ) // Caused by: java.lang.NoSuchMethodError: Micronaut method io.micronaut.context.DefaultBeanContext.getProxyTargetBean(BeanResolutionContext,BeanDefinition,Argument,Qualifier) not found. Most likely reason for this issue is that you are running a newer version of Micronaut with code compiled against an older version. Please recompile the offending classe"
    }
}
