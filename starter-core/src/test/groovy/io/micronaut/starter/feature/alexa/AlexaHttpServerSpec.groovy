package io.micronaut.starter.feature.alexa

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AlexaHttpServerSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle alexa-http-server feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['alexa-http-server'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-alexa-httpserver")')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven alexa-http-server feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['alexa-http-server'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-alexa-httpserver</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

}
