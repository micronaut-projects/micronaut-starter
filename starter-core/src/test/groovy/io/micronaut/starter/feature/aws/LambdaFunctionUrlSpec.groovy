package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Subject

class LambdaFunctionUrlSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    LambdaFunctionUrl lambdaFunctionUrl = beanContext.getBean(LambdaFunctionUrl)

    void 'aws-lambda-function-url feature is in the cloud category'() {
        expect:
        lambdaFunctionUrl.category == Category.SERVERLESS
    }
    void 'aws-lambda-function-url feature is an instance of AwsApiFeature'() {
        expect:
        lambdaFunctionUrl instanceof AwsApiFeature
        lambdaFunctionUrl instanceof LambdaTrigger
    }

    void "aws-lambda-function-url does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !lambdaFunctionUrl.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION)
    }

    void "aws-lambda-function-url supports function application type"() {
        expect:
        lambdaFunctionUrl.supports(ApplicationType.FUNCTION)
    }

    void 'Function AppStack log retention is included for #buildTool'(BuildTool buildTool) {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, createOptions(buildTool), [LambdaFunctionUrl.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.logs.RetentionDays;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.logRetention(RetentionDays.ONE_WEEK)')

        where:
        buildTool << BuildTool.values()
    }

    void 'lambda runtime main class configuration is present for #buildTool'(BuildTool buildTool) {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, createOptions(buildTool), [LambdaFunctionUrl.NAME])

        then:
        if (buildTool == BuildTool.GRADLE) {
            assert output['app/build.gradle']
            assert output['app/build.gradle'].contains('nativeLambda {')
            assert output['app/build.gradle'].contains('lambdaRuntimeClassName = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime"')
        } else if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert output['app/build.gradle.kts']
            assert output['app/build.gradle.kts'].contains('nativeLambda {')
            assert output['app/build.gradle.kts'].contains('lambdaRuntimeClassName.set("io.micronaut.function.aws.runtime.MicronautLambdaRuntime")')
        }

        where:
        buildTool << BuildTool.valuesGradle()
    }

    private static Options createOptions(BuildTool buildTool) {
        new Options(Language.JAVA, TestFramework.JUNIT, buildTool, AwsLambdaFeatureValidator.firstSupportedJdk())
    }
}
