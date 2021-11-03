package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Issue
import spock.lang.Unroll

class OracleCloudAutonomousDatabaseSpec extends ApplicationContextSpec implements CommandOutputFixture {

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

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/912")
    void 'test default database driver not present in config'() {
        when:
        def output = generate(['oracle-cloud-atp', "data-jdbc"])
        def config = output["src/main/resources/application.yml"]

        then:
        !config.contains('dialect')
        !config.contains('schema-generate')
    }
}
