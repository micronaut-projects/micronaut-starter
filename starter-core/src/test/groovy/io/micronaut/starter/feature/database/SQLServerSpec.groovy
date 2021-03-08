package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SQLServerSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle sqlserver feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['sqlserver'])
                .language(language)
                .render()

        then:
        template.contains('runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven sqlserver feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['sqlserver'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>mssql-jdbc</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
