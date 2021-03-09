package io.micronaut.starter.feature.security

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SecurityLdapSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature security-ldap contains links to micronaut docs'() {
        when:
        def output = generate(['security-ldap'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#ldap")
    }

    @Unroll
    void 'test gradle security-ldap feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['security-ldap'])
                .render()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.security:micronaut-security-annotations\")")
        template.contains('implementation("io.micronaut.security:micronaut-security-ldap")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven security-ldap feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['security-ldap'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.security</groupId>
      <artifactId>micronaut-security-ldap</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        if (language == Language.JAVA) {
            assert template.contains('''\
            <path>
              <groupId>io.micronaut.security</groupId>
              <artifactId>micronaut-security-annotations</artifactId>
              <version>${micronaut.security.version}</version>
            </path>
''')
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
}
