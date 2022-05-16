package io.micronaut.starter.feature.oracecloud

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.function.CloudProvider
import io.micronaut.starter.feature.oraclecloud.OracleCloudSdk
import io.micronaut.starter.feature.oraclecloud.OracleCloudVault
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class OracleCloudSdkSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    OracleCloudSdk oracleCloudSdk = beanContext.getBean(OracleCloudSdk)

    @Unroll
    void "oracle-cloud-vault belongs to cloud ORACLE"() {
        expect:
        oracleCloudSdk.cloudProvider.isPresent()
        CloudProvider.ORACLE == oracleCloudSdk.cloudProvider.get()
    }

    @Unroll
    void 'test Oracle Cloud SDK feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['oracle-cloud-sdk'])
                .language(language)
                .render()
        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-sdk")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test Oracle Cloud SDK feature for maven and language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['oracle-cloud-sdk'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.oraclecloud</groupId>
      <artifactId>micronaut-oraclecloud-sdk</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void 'test Oracle Cloud SDK config file'() {
        when:
        def output = generate(['oracle-cloud-sdk'])
        def config = output["src/main/resources/application.yml"]

        then:
        config.contains('oci.config.profile: DEFAULT')
    }

}
