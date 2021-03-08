package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AWSSDKv2Spec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Unroll
    void 'test Oracle Cloud SDK feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['aws-v2-sdk'])
                .render()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-sdk-v2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jmx feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['aws-v2-sdk'])
                .render()

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
