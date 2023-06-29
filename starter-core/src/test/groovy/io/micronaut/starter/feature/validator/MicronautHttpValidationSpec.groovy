package io.micronaut.starter.feature.validator

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import spock.lang.Subject

class MicronautHttpValidationSpec  extends ApplicationContextSpec {
    @Subject
    MicronautHttpValidation micronautHttpValidation = beanContext.getBean(MicronautHttpValidation)

    void 'micronaut-http-validation feature is in the cloud category'() {
        expect:
        micronautHttpValidation.category == Category.VALIDATION
    }

    void "micronaut-http-validation does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !micronautHttpValidation.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.DEFAULT)
    }

    void "micronaut-http-validation supports function application type"() {
        expect:
        micronautHttpValidation.supports(ApplicationType.DEFAULT)
    }

    void "dependency added for micronaut-http-validation feature in the main classpath"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool).features(['micronaut-http-validation']).render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-validation", Scope.ANNOTATION_PROCESSOR)

        where:
        buildTool << BuildTool.values()
    }
}
