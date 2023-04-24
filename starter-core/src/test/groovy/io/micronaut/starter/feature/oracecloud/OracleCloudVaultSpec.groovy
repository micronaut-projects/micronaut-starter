package io.micronaut.starter.feature.oracecloud

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import org.yaml.snakeyaml.Yaml
import spock.lang.Unroll

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

class OracleCloudVaultSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test README.md with feature oracle-cloud-vault contains links to Micronaut docs'() {
        when:
        def output = generate(['oracle-cloud-vault'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#vault")
        readme.contains("https://docs.oracle.com/en-us/iaas/Content/KeyManagement/home.htm")
    }

    void 'test src/main/resources/boostrap.yml with feature oracle-cloud-vault contains config'() {
        when:
        Map<String, String> output = generate(['yaml', 'oracle-cloud-vault'])
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
oci:
  vault:
    config:
      enabled: true
    vaults:
    - compartment-ocid: ''
      ocid: ''
''')

        when: 'verify YAML types are correct'
        Map<String, Object> bootstrapYml = new Yaml().load(bootstrap)

        then:
        bootstrapYml.oci.vault.config.enabled
        bootstrapYml.oci.vault.vaults instanceof List
        bootstrapYml.oci.vault.vaults.size() == 1
        bootstrapYml.oci.vault.vaults[0].'compartment-ocid' == ''
        bootstrapYml.oci.vault.vaults[0].ocid == ''
    }

    @Unroll
    void 'test Gradle oracle-cloud-vault feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE)
                .language(language)
                .features(['oracle-cloud-vault'])
                .render()

        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-vault")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test Maven oracle-cloud-vault feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, MAVEN)
                .language(language)
                .features(['oracle-cloud-vault'])
                .render()

        then:
        template.contains('''
    <dependency>
      <groupId>io.micronaut.oraclecloud</groupId>
      <artifactId>micronaut-oraclecloud-vault</artifactId>
      <scope>compile</scope>
    </dependency>
''')

        where:
        language << Language.values().toList()
    }
}
