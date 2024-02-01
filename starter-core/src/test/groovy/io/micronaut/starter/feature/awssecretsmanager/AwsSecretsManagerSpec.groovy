package io.micronaut.starter.feature.awssecretsmanager

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AwsSecretsManagerSpec extends ApplicationContextSpec implements CommandOutputFixture {
    void 'test readme.md with feature aws-secrets-manager contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['aws-secrets-manager'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/#distributedconfigurationsecretsmanager")
        readme.contains('https://aws.amazon.com/secrets-manager/')
    }

    void 'test src/main/resources/boostrap.yml with feature aws-secrets-manager contains config'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'aws-secrets-manager'])
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
    void 'test gradle aws-secrets-manager feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['aws-secrets-manager'])
                .render()

        then:
        template.count('implementation("io.micronaut.aws:micronaut-aws-secretsmanager")') == 1

        and: 'micronaut-aws-secretsmanager exposes aws-sdk-v2 transitively'
        !template.contains('implementation("io.micronaut.aws:aws-sdk-v2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven aws-secrets-manager feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['aws-secrets-manager'])
                .render()

        then:
        template.count('''
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-secretsmanager</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        where:
        language << Language.values().toList()
    }
}
