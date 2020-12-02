package io.micronaut.starter.feature.aws

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom


class AWSSDKv2Spec extends BeanContextSpec  implements CommandOutputFixture {

    @Unroll
    void 'test Oracle Cloud SDK feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-v2-sdk'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-sdk-v2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jmx feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-v2-sdk'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-sdk-v2</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

}
