package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

import java.util.stream.Collectors

class OracleSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test gradle oracle feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['oracle'])
                .language(language)
                .render()

        then:
        template.contains('runtimeOnly("com.oracle.database.jdbc:ojdbc8")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven oracle feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['oracle'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>com.oracle.database.jdbc</groupId>
      <artifactId>ojdbc8</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
