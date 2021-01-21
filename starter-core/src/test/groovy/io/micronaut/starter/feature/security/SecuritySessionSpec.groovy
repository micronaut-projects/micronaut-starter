package io.micronaut.starter.feature.security

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class SecuritySessionSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature security-session contains links to micronaut docs'() {
        when:
        def output = generate(['security-session'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#session")
    }


    @Unroll
    void 'test gradle security-session feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['security-session'], language), false).render().toString()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.security:micronaut-security-annotations\")")
        template.contains('implementation("io.micronaut.security:micronaut-security-session")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle security-session removes http-session feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['http-session','security-session'], language), false).render().toString()

        then:
        !template.contains('implementation("io.micronaut.security:micronaut-session")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven security-session feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['security-session'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.security</groupId>
      <artifactId>micronaut-security-session</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        if (language == Language.JAVA) {
            assert template.contains("""
            <path>
              <groupId>io.micronaut.security</groupId>
              <artifactId>micronaut-security-annotations</artifactId>
              <version>\${micronaut.security.version}</version>
            </path>
""")
        } else if (language == Language.KOTLIN) {
            assert template.count("""
                <annotationProcessorPath>
                  <groupId>io.micronaut.security</groupId>
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

    @Unroll
    void 'test maven security-session removes http-session feature for language=#language'() {
        when:
        def context = buildGeneratorContext(
                ['http-session','security-session'],
                new Options(language, BuildTool.MAVEN), ApplicationType.DEFAULT)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(),
                context.getFeatures(), context.getBuildProperties().getProperties()).render().toString()

        then:
        !template.contains("micronaut-session")

        where:
        language << Language.values().toList()
    }


    void 'test security-session configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-session'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'session'

        when:
        commandContext = buildGeneratorContext(['security-session', 'security-jwt'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'session'

        when:
        commandContext = buildGeneratorContext(['security-session', 'security-jwt', 'security-oauth2'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'session'
    }
}
