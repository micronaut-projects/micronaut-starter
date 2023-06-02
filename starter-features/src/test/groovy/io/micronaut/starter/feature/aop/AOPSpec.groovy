package io.micronaut.starter.feature.aop

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class AOPSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AOP micronautAOP = beanContext.getBean(AOP)

    void "AOP supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        micronautAOP.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "AOP overrides Feature->getMicronautDocumentation"() {
        expect:
        micronautAOP.micronautDocumentation
    }

    void "AOP belongs to API category"() {
        expect:
        Category.API == micronautAOP.category
    }

    void "test dependency added for AOP feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([AOP.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-aop", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
