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
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringDataJdbcSpec extends ApplicationContextSpec {

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

    void "test dependencies added for spring-data-jpa feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['spring-data-jdbc'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.spring.version', true)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-spring", Scope.COMPILE)
        verifier.hasDependency("org.springframework", "spring-jdbc", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
