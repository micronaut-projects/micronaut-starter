package io.micronaut.starter.feature.secretsmanager

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class GoogleSecretManagerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature gcp-secrets-manager contains links to micronaut docs'() {
        when:
        def output = generate(['gcp-secrets-manager'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://micronaut-projects.github.io/micronaut-gcp/latest/guide/#secretManager')
        readme.contains('https://cloud.google.com/secret-manager')
    }

    void 'test src/main/resources/boostrap.yml with feature gcp-secrets-manager contains config'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'gcp-secrets-manager'])
        String bootstrap = output["src/main/resources/bootstrap.yml"]

        then:
        bootstrap
        bootstrap.contains('''\
micronaut:
  application:
    name: foo
  config-client:
    enabled: true
''')
    }

    @Unroll
    void 'test gradle gcp-secrets-manager feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['gcp-secrets-manager'])
                .render()

        then:
        template.count('implementation("io.micronaut.gcp:micronaut-gcp-secret-manager")') == 1

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle graalvm & gcp-secrets-manager features adds gcp native-image support for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['gcp-secrets-manager', 'graalvm'])
                .render()

        then:
        template.count('implementation("io.micronaut.gcp:micronaut-gcp-secret-manager")') == 1

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }

    @Unroll
    void 'test maven gcp-secrets-manager feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['gcp-secrets-manager'])
                .render()

        then:
        template.count('''
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-secret-manager</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven graalvm & gcp-secrets-manager features adds gcp native-image support for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['gcp-secrets-manager', 'graalvm'])
                .render()

        then:
        template.count('''
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-secret-manager</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }
}
