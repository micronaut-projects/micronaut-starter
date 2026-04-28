package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject

class OpenTelemetryJaegerSpec extends ApplicationContextSpec {

    @Subject
    OpenTelemetryJaeger feature = beanContext.getBean(OpenTelemetryJaeger)

    void 'tracing-opentelemetry-jaeger feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-jaeger feature is not visible'() {
        expect:
        feature.isVisible()
    }

    void 'for grpc application type test gradle tracing-opentelemetry-jaeger feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.GRPC)
                .language(language)
                .features(['tracing-opentelemetry-jaeger', 'kapt'])
                .render()

        then:
        assertAnnotationProcessorInGradleTemplate(template, "io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation", language)
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-otlp")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-grpc")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE]].combinations()
    }

    void 'for default application type test gradle tracing-opentelemetry-jaeger feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .features(['tracing-opentelemetry-jaeger', 'kapt'])
                .render()

        then:
        assertAnnotationProcessorInGradleTemplate(template, "io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation", language)
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-otlp")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-grpc")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE]].combinations()
    }

    void 'for #applicationType test gradle tracing-opentelemetry-jaeger feature for language=#language'(Language language,
                                                                                                     BuildTool buildTool,
                                                                                                     ApplicationType applicationType) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(applicationType)
                .language(language)
                .features(['tracing-opentelemetry-jaeger', 'kapt'])
                .render()

        then:
        assertAnnotationProcessorInGradleTemplate(template, "io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation", language)
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-otlp")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        [language, buildTool, applicationType] << [
                Language.values().toList(),
                [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE],
                (ApplicationType.values().toList() - ApplicationType.GRPC - ApplicationType.DEFAULT - ApplicationType.CLI)
        ].combinations()
    }

    void 'test maven tracing-opentelemetry-jaeger feature for language=#language'(Language language) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['tracing-opentelemetry-jaeger'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("io.opentelemetry", "opentelemetry-exporter-jaeger")
        verifier.hasDependency("io.opentelemetry", "opentelemetry-exporter-otlp")
        !verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-opentelemetry")
        verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-opentelemetry-http")

        where:
        language << Language.values().toList()
    }

    void 'for function test maven tracing-opentelemetry-jaeger feature for language=#language'(Language language) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .applicationType(ApplicationType.FUNCTION)
                .language(language)
                .features(['tracing-opentelemetry-jaeger'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("io.opentelemetry", "opentelemetry-exporter-jaeger")
        verifier.hasDependency("io.opentelemetry", "opentelemetry-exporter-otlp")
        verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-opentelemetry")
        !verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-opentelemetry-http")

        where:
        language << Language.values().toList()
    }
}
