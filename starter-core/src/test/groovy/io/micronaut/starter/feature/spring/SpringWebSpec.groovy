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

class SpringWebSpec extends ApplicationContextSpec {

    @Shared
    @Subject
    SpringWeb springWeb = beanContext.getBean(SpringWeb)

    void 'spring-web belongs to Spring category'() {
        expect:
        Category.SPRING == springWeb.category
    }

    void 'spring-web is visible'() {
        expect:
        springWeb.visible
    }

    void 'spring-web title and description are different'() {
        expect:
        springWeb.getTitle()
        springWeb.getDescription()
        springWeb.getTitle() != springWeb.getDescription()
    }

    @Unroll
    void 'feature spring-web does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !springWeb.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.DEFAULT)
    }

    @Unroll
    void 'feature spring-web supports #applicationType'(ApplicationType applicationType) {
        expect:
        springWeb.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT]
    }

    void 'test spring-web features'() {
        when:
        Features features = getFeatures(['spring-web'])

        then:
        features.contains('spring')
    }

    void "test dependencies added for spring feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([SpringWeb.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-server", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-web", Scope.RUNTIME)
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter", Scope.COMPILE)
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter-web", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.spring.version', true)
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-web-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.spring.version', true)

        where:
        buildTool << BuildTool.values()
    }
}
