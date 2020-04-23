package io.micronaut.starter.feature.awslambdacustomruntime

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaCustomRuntimeSpec extends BeanContextSpec {

    @Shared
    @Subject
    AwsLambdaCustomRuntime awsLambdaCustomRuntime = beanContext.getBean(AwsLambdaCustomRuntime)

    @Unroll
    void "aws-lambda-custom-runtime does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !awsLambdaCustomRuntime.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING
        ]
        description = applicationType.name
    }

    void "aws-lambda-custom-runtime supports #description application type"() {
        expect:
        awsLambdaCustomRuntime.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.DEFAULT,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    @Unroll
    void 'test gradle aws-lambda-custom-runtime feature for language=#language'(Language language, ApplicationType applicationType) {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['aws-lambda-custom-runtime'], language, null, BuildTool.GRADLE, applicationType)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')

        where:
        language        | applicationType
        Language.JAVA   | ApplicationType.FUNCTION
        Language.GROOVY | ApplicationType.FUNCTION
        Language.KOTLIN | ApplicationType.FUNCTION
        Language.JAVA   | ApplicationType.DEFAULT
        Language.GROOVY | ApplicationType.DEFAULT
        Language.KOTLIN | ApplicationType.DEFAULT
    }

    @Unroll
    void 'test maven aws-lambda-custom-runtime feature for language=#language'(Language language, ApplicationType applicationType) {
        when:
        String template = pom.template(buildProject(), getFeatures(['aws-lambda-custom-runtime'], language, null, BuildTool.MAVEN,applicationType), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-custom-runtime</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language        | applicationType
        Language.JAVA   | ApplicationType.FUNCTION
        Language.GROOVY | ApplicationType.FUNCTION
        Language.KOTLIN | ApplicationType.FUNCTION
        Language.JAVA   | ApplicationType.DEFAULT
        Language.GROOVY | ApplicationType.DEFAULT
        Language.KOTLIN | ApplicationType.DEFAULT
    }
}
