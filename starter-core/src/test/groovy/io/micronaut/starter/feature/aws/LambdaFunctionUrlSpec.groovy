package io.micronaut.starter.feature.aws

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
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
        def build = output["app/$buildTool.buildFileName"]

        then:
        build.contains('nativeLambda {')
        build.contains('lambdaRuntimeClassName = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime"')

        where:
        buildTool << BuildTool.valuesGradle()
    }

    private static Options createOptions(BuildTool buildTool) {
        MicronautOptions.builder()
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(AwsLambdaFeatureValidator.firstSupportedJdk())
                .build()
    }
}
