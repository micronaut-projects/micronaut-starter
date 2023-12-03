package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Subject
import spock.lang.Unroll

class OpenTelemetryAnnotationsSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryAnnotations feature = beanContext.getBean(OpenTelemetryAnnotations)

    void 'tracing-opentelemetry-annotations feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-annotations feature is not visible'() {
        expect:
        !feature.isVisible()
    }

    @Unroll
    void 'feature tracing-opentelemetry-annotations does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    @Unroll
    void 'feature tracing-opentelemetry-annotations supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }

    void 'test gradle tracing-opentelemetry-annotations feature for BuildTol #buildTool'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['tracing-opentelemetry-annotations'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-opentelemetry-annotation", Scope.ANNOTATION_PROCESSOR, 'micronaut.tracing.version', true)

        where:
        buildTool << BuildTool.values()
    }
}
