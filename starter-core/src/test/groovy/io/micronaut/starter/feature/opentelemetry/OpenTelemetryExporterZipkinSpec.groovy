package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject
import spock.lang.Unroll

class OpenTelemetryExporterZipkinSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryExporterZipkin feature = beanContext.getBean(OpenTelemetryExporterZipkin)

    void 'tracing-opentelemetry-exporter-zipkin feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-exporter-zipkin feature is not visible'() {
        expect:
        !feature.isVisible()
    }

    void 'test otel.traces.exporter configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['tracing-opentelemetry-exporter-zipkin'])

        then:
        commandContext.configuration.get('otel.traces.exporter') == 'zipkin'
    }

    @Unroll
    void 'feature tracing-opentelemetry-exporter-zipkin does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    @Unroll
    void 'feature tracing-opentelemetry-exporter-zipkin supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }

    void 'test gradle tracing-opentelemetry-exporter-zipkin feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-exporter-zipkin'])
                .render()

        then:
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-zipkin")')
     
        where:
        language << Language.values().toList()
    }
    
    void 'test maven tracing-opentelemetry-exporter-zipkin feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['tracing-opentelemetry-exporter-zipkin'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-exporter-zipkin</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        where:
        language << Language.values().toList()
    }
}
