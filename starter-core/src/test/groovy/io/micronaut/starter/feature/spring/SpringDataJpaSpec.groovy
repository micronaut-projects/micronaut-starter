package io.micronaut.starter.feature.spring

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
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

    void "test dependencies added for spring-data-jpa feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['spring-data-jpa'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.spring.version', true)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-spring", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-spring-jpa", Scope.COMPILE)
        verifier.hasDependency("org.springframework", "spring-orm", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
