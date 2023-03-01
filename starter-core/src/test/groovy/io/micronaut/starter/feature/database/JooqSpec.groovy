package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language

class JooqSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jooq contains links to micronaut docs'() {
        when:
        def output = generate(['jooq'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq")
    }

    void 'test gradle jooq feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jooq'])
                .language(language)
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jooq")')

        where:
        language << Language.values().toList()
    }

    void 'test maven jooq feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['jooq'])
                .language(language)
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jooq</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void "test jooq cannot be applied for #language with Java 8"() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jooq'])
                .language(language)
                .jdkVersion(JdkVersion.JDK_8)
                .render()

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "The selected feature jooq requires at latest Java 11"

        where:
        language << Language.values().toList()
    }
}
