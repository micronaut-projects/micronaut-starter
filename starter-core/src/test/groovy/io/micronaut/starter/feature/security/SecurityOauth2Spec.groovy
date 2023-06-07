package io.micronaut.starter.feature.security

import io.micronaut.context.env.Environment
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SecurityOauth2Spec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature security-oauth2 contains links to micronaut docs'() {
        when:
        def output = generate(['security-oauth2'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#oauth")
    }

    @Unroll
    void 'test gradle security-oauth2 feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['security-oauth2', 'kapt'])
                .render()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.security:micronaut-security-annotations\")")
        template.contains('implementation("io.micronaut.security:micronaut-security-oauth2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven security-oauth2 feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['security-oauth2'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.security</groupId>
      <artifactId>micronaut-security-oauth2</artifactId>
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

    void 'test security-oauth2 configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-oauth2'])

        then:
        commandContext.configuration.get("micronaut.security.authentication") == "cookie"
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-id")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-secret")
        !commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.openid.issuer")

        when:
        commandContext = buildGeneratorContext(['security-oauth2', 'security-jwt'])

        then:
        commandContext.configuration.get("micronaut.security.authentication") == "cookie"
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-id")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-secret")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.openid.issuer")
    }
}
