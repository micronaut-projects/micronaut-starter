package io.micronaut.starter.feature.awssecretsmanager

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
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
        when:
        Map<String, Object> bootstrapYml = new org.yaml.snakeyaml.Yaml().load(bootstrap)

        then:
        'foo' == bootstrapYml['micronaut']['application']['name']
        true == bootstrapYml['micronaut']['config-client']['enabled']
        true == bootstrapYml['aws']['client']['system-manager']['parameterstore']['enabled']
        false == bootstrapYml['aws']['distributed-configuration']['search-active-environments']
        false == bootstrapYml['aws']['distributed-configuration']['search-common-application']
    }

    @Unroll
    void 'test #buildTool aws-secrets-manager feature for language=#language'(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['aws-secrets-manager'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-secretsmanager", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut.aws", ":aws-sdk-v2", Scope.COMPILE)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values().toList()].combinations()
    }
}
