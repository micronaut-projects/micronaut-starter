package io.micronaut.starter.feature.security

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SecurityOauth2Spec extends BeanContextSpec {

    @Unroll
    void 'test gradle security-oauth2 feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['security-oauth2'], language)).render().toString()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut:micronaut-security-annotations\")")
        template.contains('implementation("io.micronaut.configuration:micronaut-security-oauth2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven security-oauth2 feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['security-oauth2'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-security-oauth2</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        if (language == Language.JAVA) {
            assert template.contains("""
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-security-annotations</artifactId>
              <version>\${micronaut.security.version}</version>
            </path>
""")
            assert template.contains("""
                <path>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-security-annotations</artifactId>
                  <version>\${micronaut.security.version}</version>
                </path>
""")
        } else if (language == Language.KOTLIN) {
            assert template.count("""
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-security-annotations</artifactId>
                  <version>\${micronaut.security.version}</version>
                </annotationProcessorPath>
""") == 2
        } else if (language == Language.GROOVY) {
            assert true
        } else {
            assert false
        }

        where:
        language << Language.values().toList()
    }

    void 'test security-oauth2 configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-oauth2', 'security-jwt'])

        then:
        commandContext.configuration.get("micronaut.security.token.jwt.cookie.enabled") == true
    }
}
