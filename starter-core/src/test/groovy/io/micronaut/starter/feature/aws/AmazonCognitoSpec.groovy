package io.micronaut.starter.feature.aws

import io.micronaut.context.env.Environment
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Category
import spock.lang.Subject

class AmazonCognitoSpec extends ApplicationContextSpec {

    @Subject
    AmazonCognito amazonCognito = beanContext.getBean(AmazonCognito)

    void 'test amazon-cognito configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['amazon-cognito'])

        then:
        commandContext.configuration.containsKey("micronaut.security.authentication")
        commandContext.configuration.get("micronaut.security.authentication") == 'idtoken'
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-id")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-secret")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.openid.issuer")
        commandContext.getConfiguration(Environment.DEVELOPMENT).get('micronaut.security.oauth2.clients.default.openid.issuer') ==  'https://cognito-idp.${COGNITO_REGION:us-east-1}.amazonaws.com/${COGNITO_POOL_ID:ZZZ}/'
    }

    void 'amazon-cognito feature is in the cloud category'() {
        expect:
        amazonCognito.category == Category.SECURITY
    }

    void 'amazon-cognito feature is an instance of AwsLambdaEventFeature'() {
        expect:
        amazonCognito instanceof AwsFeature
    }

    void "amazon-cognito does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !amazonCognito.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.DEFAULT)
    }

    void "amazon-cognito supports function application type"() {
        expect:
        amazonCognito.supports(ApplicationType.DEFAULT)
    }
}
