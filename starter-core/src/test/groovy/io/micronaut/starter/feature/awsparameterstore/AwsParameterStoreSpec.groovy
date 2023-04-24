package io.micronaut.starter.feature.awsparameterstore

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AwsParameterStoreSpec extends ApplicationContextSpec implements CommandOutputFixture {
    void 'test readme.md with feature aws-parameter-store contains links to micronaut docs'() {
        when:
        def output = generate(['aws-parameter-store'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#parametersStore")
        readme.contains('https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html')
    }

    void 'test src/main/resources/boostrap.yml with feature aws-parameter-store contains config'() {
        when:
        Map<String, String> output = generate(['yaml', 'aws-parameter-store'])
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
        bootstrap.contains('''\
aws.client.system-manager.parameterstore.enabled: true
''')
    }

    @Unroll
    void 'test gradle aws-parameter-store feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['aws-parameter-store'])
                .render()

        then:
        template.count('implementation("io.micronaut.aws:micronaut-aws-parameter-store")') == 1

        and: 'micronaut-aws-parameter-store exposes aws-sdk-v2 transitively'
        !template.contains('implementation("io.micronaut.aws:aws-sdk-v2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven aws-parameter-store feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['aws-parameter-store'])
                .render()

        then:
        template.count('''
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-parameter-store</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        where:
        language << Language.values().toList()
    }
}
