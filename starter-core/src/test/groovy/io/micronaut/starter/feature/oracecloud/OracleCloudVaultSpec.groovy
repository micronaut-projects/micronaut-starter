package io.micronaut.starter.feature.oracecloud

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

class OracleCloudVaultSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test README.md with feature oracle-cloud-vault contains links to Micronaut docs'() {
        when:
        Map<String, String> output = generate(['oracle-cloud-vault'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#vault")
        readme.contains("https://docs.oracle.com/en-us/iaas/Content/KeyManagement/home.htm")
    }

    void 'test src/main/resources/application.yml with feature oracle-cloud-vault contains config import'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'oracle-cloud-vault'])
        String application = output["src/main/resources/application.yml"]

        then:
        application
        !output.containsKey("src/main/resources/bootstrap.yml")

        when: 'verify YAML types are correct'

        Map<String, Object> applicationYml = new org.yaml.snakeyaml.Yaml().load(application)

        then:
        applicationYml.micronaut.config.import.provider == 'oraclecloud-vault'
        applicationYml.micronaut.config.import.'config-profile' == 'DEFAULT'
        applicationYml.micronaut.config.import.ocid == ''
        applicationYml.micronaut.config.import.'compartment-id' == ''
        applicationYml.'oci.config.profile' == 'DEFAULT'
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
        language << supportedLanguages(BuildTool.MAVEN)
    }
}
