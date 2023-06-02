package io.micronaut.starter.feature.security

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SecurityJWTSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature security-jwt contains links to micronaut docs'() {
        when:
        def output = generate(['security-jwt'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle security-jwt feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['security-jwt', 'kapt'])
                .render()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.security:micronaut-security-annotations\")")
        !template.contains(getGradleAnnotationProcessorScope(language) + '("io.micronaut.security:micronaut-security-annotations:${micronaut.security.version}")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven security-jwt feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features( ['security-jwt'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.security</groupId>
      <artifactId>micronaut-security-jwt</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        if (language == Language.JAVA) {
            assert template.count("""
            <path>
              <groupId>io.micronaut.security</groupId>
              <artifactId>micronaut-security-annotations</artifactId>
              <version>\${micronaut.security.version}</version>
            </path>
""") == 1
        } else if (language == Language.KOTLIN) {
            assert template.count('''\
               <annotationProcessorPath>
                 <groupId>io.micronaut.security</groupId>
                 <artifactId>micronaut-security-annotations</artifactId>
                 <version>${micronaut.security.version}</version>
               </annotationProcessorPath>
''') == 1
        } else if (language == Language.GROOVY) {
            assert true
        } else {
            assert false
        }

        where:
        language << Language.values().toList()
    }

    void 'test security-jwt configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-jwt'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'bearer'
        commandContext.configuration.get('micronaut.security.token.jwt.signatures.secret.generator.secret'.toString()) == '${JWT_GENERATOR_SIGNATURE_SECRET:pleaseChangeThisSecretForANewOne}'
    }
}
