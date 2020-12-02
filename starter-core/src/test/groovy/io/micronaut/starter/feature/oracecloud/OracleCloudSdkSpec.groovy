package io.micronaut.starter.feature.oracecloud

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class OracleCloudSdkSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Unroll
    void 'test Oracle Cloud SDK feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['oracle-cloud-sdk'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-sdk")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test Oracle SDK feature for maven and language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['oracle-cloud-sdk'], language), []).render().toString()

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

}
