package io.micronaut.starter.feature.security

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SecuritySessionSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle security-session feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['security-session'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut:micronaut-security-session"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven security-session feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['security-session'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-security-session</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    void 'test security-session configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['security-session'])

        then:
        commandContext.configuration.get('micronaut.security.endpoints.login.enabled'.toString()) == true
        commandContext.configuration.get('micronaut.security.endpoints.logout.enabled'.toString()) == true
        commandContext.configuration.get('micronaut.security.session.loginSuccessTargetUrl'.toString()) == '/'
        commandContext.configuration.get('micronaut.security.session.loginFailureTargetUrl'.toString()) == '/'
    }
}
