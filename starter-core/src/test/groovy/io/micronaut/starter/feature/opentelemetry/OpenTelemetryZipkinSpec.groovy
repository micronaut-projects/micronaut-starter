package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject

class OpenTelemetryZipkinSpec extends ApplicationContextSpec {

    @Subject
    OpenTelemetryZipkin feature = beanContext.getBean(OpenTelemetryZipkin)

    void 'tracing-opentelemetry-zipkin feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-zipkin feature is not visible'() {
        expect:
        feature.isVisible()
    }

    void 'for grpc application type test gradle tracing-opentelemetry-zipkin feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.GRPC)
                .language(language)
                .features(['tracing-opentelemetry-zipkin'])
                .render()

        then:
        assertAnnotationProcessorInGradleTemplate(template, "io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation", language)
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-zipkin")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-grpc")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE]].combinations()
    }

    void 'for default application type test gradle tracing-opentelemetry-zipkin feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .features(['tracing-opentelemetry-zipkin'])
                .render()

        then:
        assertAnnotationProcessorInGradleTemplate(template, "io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation", language)
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-zipkin")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-grpc")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE]].combinations()
    }

    void 'for #applicationType test gradle tracing-opentelemetry-zipkin feature for language=#language'(Language language,
                                                                                                        BuildTool buildTool,
                                                                                                        ApplicationType applicationType) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(applicationType)
                .language(language)
                .features(['tracing-opentelemetry-zipkin'])
                .render()

        then:
        assertAnnotationProcessorInGradleTemplate(template, "io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation", language)
        template.contains('implementation("io.opentelemetry:opentelemetry-exporter-zipkin")')
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")')
        !template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        [language, buildTool, applicationType] << [
                Language.values().toList(),
                [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE],
                (ApplicationType.values().toList() - ApplicationType.GRPC - ApplicationType.DEFAULT - ApplicationType.CLI)
        ].combinations()
    }

    void 'test maven tracing-opentelemetry-zipkin feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['tracing-opentelemetry-zipkin'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-exporter-zipkin</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        !template.contains("""
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry-http</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        where:
        language << Language.values().toList()
    }

    void 'for function test maven tracing-opentelemetry-zipkin feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .applicationType(ApplicationType.FUNCTION)
                .language(language)
                .features(['tracing-opentelemetry-zipkin'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-exporter-zipkin</artifactId>
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
