package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class PostgreSQLSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle postgres feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['postgres'])
                .language(language)
                .render()
        then:
        template.contains('runtimeOnly("org.postgresql:postgresql")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven postgres feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['postgres'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
