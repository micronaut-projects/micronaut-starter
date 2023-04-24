package io.micronaut.starter.feature.oracecloud

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Issue
import spock.lang.Unroll

class OracleCloudAutonomousDatabaseSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test ATP feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(JdkVersion.JDK_11)
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
                .jdkVersion(JdkVersion.JDK_11)
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
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['yaml', 'oracle-cloud-atp'])
        String config = output["src/main/resources/application.yml"]

        then:
        config.contains("datasources") //default jdbc is added
        config.contains("""
    ocid: ''
    walletPassword: ''
""")
        !config.contains("""
    username: ''
    password: ''
""")
    }

    void 'test ATP config file no jdbc config'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['yaml', 'jdbc-hikari', 'oracle-cloud-atp'])
        String config = output["src/main/resources/application.yml"]

        then:
        !config.contains('driver-class-name')
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/912")
    void 'test default database driver not present in config'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['yaml', 'oracle-cloud-atp', "data-jdbc"])
        String config = output["src/main/resources/application.yml"]

        then:
        !config.contains('dialect: H2')
    }

    void 'test config with a driver config feature'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['yaml', 'oracle-cloud-atp', "data-jdbc"])
        String config = output["src/main/resources/application.yml"]

        then:
        config.contains("""
    ocid: ''
    walletPassword: ''
""")
        !config.contains("""
    username: ''
    password: ''
""")
        config.contains("""
    schema-generate: CREATE_DROP
    dialect: ORACLE
""")
    }


    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/942")
    void 'test oracle-cloud-atp requires java 11 or higher'() {
        when:
        generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_8),
                ['oracle-cloud-atp'])

        then:
        thrown(IllegalArgumentException)

        when:
        generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_11),
                ['oracle-cloud-atp'])
        then:
        noExceptionThrown()

        when:
        generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN, JdkVersion.JDK_17),
                ['oracle-cloud-atp'])
        then:
        noExceptionThrown()
    }
}
