package io.micronaut.starter.feature.validator

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Dependency
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

    void "dependency added for micronaut-http-validation is aligned with micronaut core version"() {
        given:
        GeneratorContext context = Mock()
        List<Dependency> dependencies = []

        when:
        micronautHttpValidation.apply(context)

        then:
        1 * context.addDependency(_) >> (args) -> { dependencies << args[0] }
        0 * _

        and:
        dependencies.stream().anyMatch {
            it.artifactId == "micronaut-http-validation" &&
            it.versionProperty == "micronaut.core.version" &&
            it.scope == Scope.ANNOTATION_PROCESSOR
        }
    }
}
