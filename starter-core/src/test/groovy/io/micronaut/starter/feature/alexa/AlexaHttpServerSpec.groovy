package io.micronaut.starter.feature.alexa

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AlexaHttpServerSpec extends BeanContextSpec {

    @Shared
    @Subject
    AlexaHttpServer alexaHttpServer = beanContext.getBean(AlexaHttpServer)

    @Unroll
    void "alexa-http-server does not support #description"(ApplicationType applicationType,
                                                           String description) {
        expect:
        !alexaHttpServer.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    void "alexa-http-server supports default application type"() {
        expect:
        alexaHttpServer.supports(ApplicationType.DEFAULT)
    }

    @Unroll
    void 'test gradle alexa-http-server feature for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['alexa-http-server'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-alexa-httpserver")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven alexa-http-server feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['alexa-http-server'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-alexa-httpserver</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList().toList()
    }

}
