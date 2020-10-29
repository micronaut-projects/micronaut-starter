package io.micronaut.starter.feature.multitenancy

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class MultitenancyGormSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Subject
    @Shared
    MultitenancyGorm multitenancy = beanContext.getBean(MultitenancyGorm)

    void 'test readme.md with feature multi-tenancy-gorm contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.GROOVY, TestFramework.SPOCK)
        def output = generate(ApplicationType.DEFAULT, options, ['multi-tenancy-gorm'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#multitenancyGorm")
        readme.contains("http://gorm.grails.org/latest/hibernate/manual/index.html#multiTenancy")
    }

    void "multi-tenancy-gorm belongs to Database category"() {
        expect:
        Category.DATABASE == multitenancy.category
    }

    void "multi-tenancy-gorm title and description are different"() {
        expect:
        multitenancy.getTitle()
        multitenancy.getDescription()
        multitenancy.getTitle() != multitenancy.getDescription()
    }

    @Unroll("feature multi-tenancy-gorm works for application type: #applicationType")
    void "feature multi-tenancy-gorm works for every type of application type"(ApplicationType applicationType) {
        expect:
        multitenancy.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'dependency is included with maven and feature multi-tenancy-gorm for groovy'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['multi-tenancy-gorm'], Language.GROOVY), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.groovy</groupId>
      <artifactId>micronaut-multitenancy-gorm</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        and:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-multitenancy</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

    void 'dependency is included with gradle and feature multi-tenancy-gorm for groovy'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['multi-tenancy-gorm'], Language.GROOVY), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-multitenancy-gorm")')
        and:
        template.contains('implementation("io.micronaut:micronaut-multitenancy")')
    }
}
