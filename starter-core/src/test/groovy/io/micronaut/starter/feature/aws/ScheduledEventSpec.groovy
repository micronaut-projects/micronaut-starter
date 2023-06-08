package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import spock.lang.Subject

class ScheduledEventSpec extends ApplicationContextSpec {

    @Subject
    AwsLambdaScheduledEvent awsLambdaScheduledEvent = beanContext.getBean(AwsLambdaScheduledEvent)

    void 'aws-lambda-scheduled-event feature is in the cloud category'() {
        expect:
        awsLambdaScheduledEvent.category == Category.SERVERLESS
    }

    void 'aws-lambda-scheduled-event feature is an instance of AwsLambdaEventFeature'() {
        expect:
        awsLambdaScheduledEvent instanceof AwsLambdaEventFeature
        awsLambdaScheduledEvent instanceof AwsFeature
        awsLambdaScheduledEvent instanceof LambdaTrigger
    }

    void "aws-lambda-scheduled-event does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !awsLambdaScheduledEvent.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION)
    }

    void "aws-lambda-scheduled-event supports function application type"() {
        expect:
        awsLambdaScheduledEvent.supports(ApplicationType.FUNCTION)
    }
}
