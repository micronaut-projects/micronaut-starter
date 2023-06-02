package io.micronaut.starter.feature.jdbi

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class JdbiFeatureSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature sql-jdbi contains links to micronaut docs'() {
        when:
        def output = generate(['sql-jdbi'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbi")
    }

    @Subject
    @Shared
    JdbiFeature jdbiFeature = beanContext.getBean(JdbiFeature)

    @Unroll("feature sql-jdbi works for application type: #applicationType")
    void "feature sql-jdbi works for every type of application type"(ApplicationType applicationType) {
        expect:
        jdbiFeature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature sql-jdbi for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['sql-jdbi'])
                .render()
        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbi</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature sql-jdbi for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['sql-jdbi'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jdbi")')

        where:
        language << Language.values()
    }
}
