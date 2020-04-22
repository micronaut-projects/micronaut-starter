package io.micronaut.starter.feature.alexafunction

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AlexaFunctionSpec extends BeanContextSpec {

    @Shared
    @Subject
    AlexaFunction alexaFunction = beanContext.getBean(AlexaFunction)

    @Unroll
    void "alexa-function does not support #description"(ApplicationType applicationType,
                                                        String description) {
        expect:
        !alexaFunction.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.DEFAULT,
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING
        ]
        description = applicationType.name
    }

    void "alexa-function supports function application type"() {
        expect:
        alexaFunction.supports(ApplicationType.FUNCTION)
    }

    @Unroll
    void 'test gradle alexa-function feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['alexa-function'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-alexa")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven alexa-function feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['alexa-function'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-alexa</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }
}