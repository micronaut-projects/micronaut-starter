package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject

class OpenTelemetryGrpcSpec extends ApplicationContextSpec {

    @Subject
    OpenTelemetryGrpc feature = beanContext.getBean(OpenTelemetryGrpc)

    void 'tracing-opentelemetry-grpc feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-grpc feature is not visible'() {
        expect:
        !feature.isVisible()
    }

    void 'test maven tracing-opentelemetry-grpc feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .applicationType(ApplicationType.GRPC)
                .language(language)
                .features(['tracing-opentelemetry-grpc'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-opentelemetry-grpc", Scope.COMPILE)
        where:
        language << Language.values().toList()
    }
}
