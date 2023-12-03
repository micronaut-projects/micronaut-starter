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

class SpringBootSpec extends ApplicationContextSpec {

    @Shared
    @Subject
    SpringBoot springBoot = beanContext.getBean(SpringBoot)

    void 'spring-boot belongs to Spring category'() {
        expect:
        Category.SPRING == springBoot.category
    }

    void 'spring-boot is visible'() {
        expect:
        springBoot.visible
    }

    void 'spring-boot title and description are different'() {
        expect:
        springBoot.getTitle()
        springBoot.getDescription()
        springBoot.getTitle() != springBoot.getDescription()
    }

    @Unroll
    void 'feature spring-boot supports every type of application type. applicationType=#applicationType'() {
        expect:
        springBoot.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void 'test spring-boot features'() {
        when:
        Features features = getFeatures(['spring-boot'])

        then:
        features.contains('spring')
    }

    void "test dependencies added for spring-data-jpa feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['spring-boot'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.spring.version', true)
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter-web", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-boot", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }
}
