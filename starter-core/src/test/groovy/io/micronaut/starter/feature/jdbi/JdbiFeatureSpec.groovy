package io.micronaut.starter.feature.jdbi

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class JdbiFeatureSpec extends BeanContextSpec  implements CommandOutputFixture {

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
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['sql-jdbi'], language), []).render().toString()

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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['sql-jdbi'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jdbi")')

        where:
        language << Language.values()
    }
}
