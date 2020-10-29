package io.micronaut.starter.feature.multitenancy

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class MultitenancySpec extends BeanContextSpec  implements CommandOutputFixture {

    @Subject
    @Shared
    Multitenancy multitenancy = beanContext.getBean(Multitenancy)

    void 'test readme.md with feature multi-tenancy contains links to micronaut docs'() {
        when:
        def output = generate(['multi-tenancy'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#multitenancy")
    }

    void "multi-tenancy belongs to Database category"() {
        expect:
        Category.DATABASE == multitenancy.category
    }

    void "multi-tenancy title and description are different"() {
        expect:
        multitenancy.getTitle()
        multitenancy.getDescription()
        multitenancy.getTitle() != multitenancy.getDescription()
    }

    @Unroll("feature multi-tenancy works for application type: #applicationType")
    void "feature multi-tenancy works for every type of application type"(ApplicationType applicationType) {
        expect:
        multitenancy.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature multi-tenancy for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['multi-tenancy'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-multitenancy</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature multi-tenancy for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['multi-tenancy'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-multitenancy")')

        where:
        language << Language.values()
    }
}
