package io.micronaut.starter.feature.netflix

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ArchaiusSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle netflix-archaius feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['netflix-archaius'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.netflix:micronaut-netflix-archaius")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven netflix-archaius feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['netflix-archaius'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.netflix</groupId>
      <artifactId>micronaut-netflix-archaius</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
