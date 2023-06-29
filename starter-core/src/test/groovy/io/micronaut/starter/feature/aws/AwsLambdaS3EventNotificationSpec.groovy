package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import spock.lang.Subject

class AwsLambdaS3EventNotificationSpec extends ApplicationContextSpec {

    @Subject
    AwsLambdaS3EventNotification awsLambdaS3EventNotification = beanContext.getBean(AwsLambdaS3EventNotification)

    void 'aws-lambda-s3-event-notification feature is in the cloud category'() {
        expect:
        awsLambdaS3EventNotification.category == Category.SERVERLESS
    }

    void 'aws-lambda-s3-event-notification feature is an instance of AwsLambdaEventFeature'() {
        expect:
        awsLambdaS3EventNotification instanceof AwsLambdaEventFeature
        awsLambdaS3EventNotification instanceof AwsFeature
        awsLambdaS3EventNotification instanceof LambdaTrigger
    }

    void "aws-lambda-s3-event-notification does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !awsLambdaS3EventNotification.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION)
    }

    void "aws-lambda-s3-event-notification supports function application type"() {
        expect:
        awsLambdaS3EventNotification.supports(ApplicationType.FUNCTION)
    }
}
