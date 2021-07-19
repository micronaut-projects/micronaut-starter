package io.micronaut.starter.feature.atp

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AtpSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test ATP feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['oracle-cloud-atp'])
                .language(language)
                .render()
        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-atp")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test ATP feature for maven and language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['oracle-cloud-atp'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.oraclecloud</groupId>
      <artifactId>micronaut-oraclecloud-atp</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void 'test ATP config file'() {
        when:
        def output = generate(['oracle-cloud-atp'])
        def config = output["src/main/resources/application.yml"]

        then:
        config.contains("""
datasources:
  default:
    ocid: ''
    walletPassword: ''
    username: ''
    password: ''
""")
    }

    void 'test ATP config file no jdbc config'() {
        when:
        def output = generate(['jdbc-hikari', 'oracle-cloud-atp'])
        def config = output["src/main/resources/application.yml"]

        then:
        !config.contains('driverClassName')
    }
}
