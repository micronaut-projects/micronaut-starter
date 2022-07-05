package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject
import spock.lang.Unroll

class OpenTelemetryExporterGoogleCloudTraceSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryExporterGoogleCloudTrace feature = beanContext.getBean(OpenTelemetryExporterGoogleCloudTrace)

    void 'tracing-opentelemetry-exporter-gcp feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    @Unroll
    void 'feature tracing-opentelemetry-exporter-gcp does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    @Unroll
    void 'feature tracing-opentelemetry-exporter-gcp supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }

    void 'test gradle tracing-opentelemetry-exporter-gcp feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-exporter-gcp'])
                .render()

        then:
        template.contains('implementation("com.google.cloud.opentelemetry:exporter-auto")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle tracing-opentelemetry-exporter-gcp  feature does not add OpenTelemetry if OpenTelemetryHttp is present for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-exporter-gcp', 'tracing-opentelemetry-http'])
                .render()

        then:
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        language << Language.values().toList()
    }

    void 'test maven tracing-opentelemetry-exporter-gcp feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['tracing-opentelemetry-exporter-gcp'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>com.google.cloud.opentelemetry</groupId>
      <artifactId>exporter-auto</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        !template.contains("""
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry-http</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        where:
        language << Language.values().toList()
    }
}
