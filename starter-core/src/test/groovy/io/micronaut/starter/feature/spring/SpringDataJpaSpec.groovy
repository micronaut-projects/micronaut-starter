package io.micronaut.starter.feature.spring

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringDataJpaSpec extends ApplicationContextSpec {

    @Shared
    @Subject
    SpringDataJpa springDataJpa = beanContext.getBean(SpringDataJpa)

    void 'spring-data-jpa belongs to Spring category'() {
        expect:
        Category.SPRING == springDataJpa.category
    }

    void 'spring-data-jpa is visible'() {
        expect:
        springDataJpa.visible
    }

    void 'spring-data-jpa title and description are different'() {
        expect:
        springDataJpa.getTitle()
        springDataJpa.getDescription()
        springDataJpa.getTitle() != springDataJpa.getDescription()
    }

    @Unroll
    void 'feature spring-data-jpa supports every type of application type. applicationType=#applicationType'() {
        expect:
        springDataJpa.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void 'test spring-data-jpa features'() {
        when:
        Features features = getFeatures(['spring-data-jpa'])

        then:
        features.contains('data-jpa')
        features.contains('spring')
    }

    @Unroll
    void 'test spring-data-jpa with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['spring-data-jpa'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.data:micronaut-data-spring")')
        template.contains('implementation("io.micronaut.data:micronaut-data-spring-jpa")')
        template.contains('implementation("org.springframework:spring-orm")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven spring-data-jpa feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-data-jpa'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-spring</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-spring-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-orm</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
