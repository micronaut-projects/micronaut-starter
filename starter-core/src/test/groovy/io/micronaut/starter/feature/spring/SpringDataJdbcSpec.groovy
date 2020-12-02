package io.micronaut.starter.feature.spring

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringDataJdbcSpec extends BeanContextSpec {

    @Shared
    @Subject
    SpringDataJdbc springDataJdbc = beanContext.getBean(SpringDataJdbc)

    void 'spring-data-jdbc belongs to Spring category'() {
        expect:
        Category.SPRING == springDataJdbc.category
    }

    void 'spring-data-jdbc is visible'() {
        expect:
        springDataJdbc.visible
    }

    void 'spring-data-jdbc title and description are different'() {
        expect:
        springDataJdbc.getTitle()
        springDataJdbc.getDescription()
        springDataJdbc.getTitle() != springDataJdbc.getDescription()
    }

    @Unroll
    void 'feature spring-data-jdbc supports every type of application type. applicationType=#applicationType'() {
        expect:
        springDataJdbc.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void 'test spring-data-jdbc features'() {
        when:
        Features features = getFeatures(['spring-data-jdbc'])

        then:
        features.contains('data-jdbc')
        features.contains('spring')
    }

    @Unroll
    void 'test spring-data-jdbc with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-data-jdbc'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.data:micronaut-data-spring")')
        template.contains('implementation("org.springframework:spring-jdbc")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven spring-data-jdbc feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-data-jdbc'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-spring</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
