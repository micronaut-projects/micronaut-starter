package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class MariaDBSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle mariadb feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['mariadb'])
                .language(language)
                .render()

        then:
        template.contains('runtimeOnly("org.mariadb.jdbc:mariadb-java-client")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven mariadb feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['mariadb'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.mariadb.jdbc</groupId>
      <artifactId>mariadb-java-client</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
