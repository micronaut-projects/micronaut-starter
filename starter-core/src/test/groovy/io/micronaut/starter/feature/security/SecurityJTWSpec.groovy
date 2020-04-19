package io.micronaut.starter.feature.security

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class SecurityJTWSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle security-jwt feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['security-jwt'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut:micronaut-security-jwt"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven security-jwt feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['security-jwt'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-security-jwt</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test security-jwt configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['security-jwt'])

        then:
        commandContext.configuration.get('micronaut.security.endpoints.login.enabled'.toString()) == true
        commandContext.configuration.get('micronaut.security.endpoints.oauth.enabled'.toString()) == true
        commandContext.configuration.get('micronaut.security.token.jwt.signatures.secret.generator.secret'.toString()) == '"${JWT_GENERATOR_SIGNATURE_SECRET:pleaseChangeThisSecretForANewOne}"'
    }
}
