package io.micronaut.starter.feature.validator

import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.projectgen.micronaut.features.validation.MicronautHttpValidation
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Options
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import spock.lang.Subject

class MicronautHttpValidationSpec  extends ApplicationContextSpec {
    @Subject
    MicronautHttpValidation micronautHttpValidation = beanContext.getBean(MicronautHttpValidation)

    void 'micronaut-http-validation feature is in the cloud category'() {
        expect:
        micronautHttpValidation.category == Category.VALIDATION
    }

    void "dependency added for micronaut-http-validation feature in the main classpath"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool).features(['micronaut-http-validation']).render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-validation", Scope.ANNOTATION_PROCESSOR)
        if (buildTool.isGradle()) {
            assert verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE_ONLY)
        }

        where:
        buildTool << BuildTool.values()
    }

    void "dependency added for micronaut-http-validation is aligned with micronaut core version"() {
        when:
        String template = new BuildBuilder(beanContext, buildTool).features(['micronaut-http-validation']).render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-validation", Scope.ANNOTATION_PROCESSOR, 'micronaut.core.version', true)

        where:
        buildTool << BuildTool.values()
    }
}
