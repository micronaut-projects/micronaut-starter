package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Subject
import spock.lang.Unroll

class OpenTelemetryExporterOtlpSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryExporterOtlp feature = beanContext.getBean(OpenTelemetryExporterOtlp)

    void 'tracing-opentelemetry-exporter-otlp feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    @Unroll
    void 'feature tracing-opentelemetry-exporter-otlp does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    @Unroll
    void 'feature tracing-opentelemetry-exporter-otlp supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }
}
