package io.micronaut.starter.feature.spring

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.security.SecurityOAuth2
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    Spring spring = beanContext.getBean(Spring)

    void 'test readme.md with feature spring contains links to micronaut docs'() {
        when:
        def output = generate(['spring'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-spring/latest/guide/index.html")
    }

    void 'spring belongs to Spring category'() {
        expect:
        Category.SPRING == spring.category
    }

    void 'spring is visible'() {
        expect:
        spring.visible
    }

    void 'spring title and description are different'() {
        expect:
        spring.getTitle()
        spring.getDescription()
        spring.getTitle() != spring.getDescription()
    }

    @Unroll
    void 'feature spring supports every type of application type. applicationType=#applicationType'() {
        expect:
        spring.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void "test dependencies added for spring feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Spring.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.springframework.boot", "spring-boot-starter", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.spring", "micronaut-spring-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.spring.version', true)

        where:
        buildTool << BuildTool.values()
    }
}
