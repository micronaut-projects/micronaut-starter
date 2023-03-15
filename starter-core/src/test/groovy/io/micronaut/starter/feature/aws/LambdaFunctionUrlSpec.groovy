package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Subject

class LambdaFunctionUrlSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    LambdaFunctionUrl lambdaFunctionUrl = beanContext.getBean(LambdaFunctionUrl)

    void 'aws-lambda-function-url feature is in the cloud category'() {
        expect:
        lambdaFunctionUrl.category == Category.CLOUD
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

    void 'Function AppStack log retention is included for #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [LambdaFunctionUrl.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.logs.RetentionDays;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.logRetention(RetentionDays.ONE_WEEK)')

        where:
        buildTool << BuildTool.values()
    }
}
