package io.micronaut.starter.feature.awslambda

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaSpec extends BeanContextSpec {

    @Shared
    @Subject
    AwsLambda awsLambda = beanContext.getBean(AwsLambda)

    @Unroll
    void "aws-lambda does not support #description"(ApplicationType applicationType,
                                                    String description) {
        expect:
        !awsLambda.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.DEFAULT,
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING
        ]
        description = applicationType.name
    }

    void "aws-lambda supports function application type"() {
        expect:
        awsLambda.supports(ApplicationType.FUNCTION)
    }

    @Unroll
    void 'test gradle aws-lambda feature for language=#language'(Language language) {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-function-aws")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven aws-lambda feature for language=#language'(Language language) {
        when:
        String template = pom.template(buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-function-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }
}
