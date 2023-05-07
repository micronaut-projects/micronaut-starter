package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.See
import spock.lang.Subject
import spock.lang.Unroll

class OpenTelemetryXraySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryXray feature = beanContext.getBean(OpenTelemetryXray)

    void 'tracing-opentelemetry-xray feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    @Unroll
    void 'feature tracing-opentelemetry-xray does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['tracing-opentelemetry-xray'])

        then:
        ctx.configuration.containsKey('otel.traces.propagator')
        "tracecontext, baggage, xray" == ctx.configuration.otel.traces.propagator
    }

    @Unroll
    void 'feature tracing-opentelemetry-xray supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }

    void 'test gradle tracing-opentelemetry-xray feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-xray'])
                .render()

        then:
        template.contains('implementation("io.opentelemetry.contrib:opentelemetry-aws-xray")')
        template.contains('implementation("io.opentelemetry:opentelemetry-extension-aws")')

        where:
        language << Language.values().toList()
    }

    @See("https://aws-otel.github.io/docs/getting-started/java-sdk/trace-manual-instr#instrumenting-the-aws-sdk")
    void 'test gradle tracing-opentelemetry-xray dynamodb features for language=#language include aws-sdk instrumentation opentelemetry dependency'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-xray', 'dynamodb'])
                .render()

        then:
        template.contains('implementation platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:1.14.0-alpha")')
        template.contains('implementation("io.opentelemetry.instrumentation:opentelemetry-aws-sdk-2.2")')
        template.contains('implementation("io.opentelemetry.contrib:opentelemetry-aws-xray")')
        template.contains('implementation("io.opentelemetry:opentelemetry-extension-aws")')

        where:
        language << Language.values().toList()
    }

    void 'for grpc application type test gradle tracing-opentelemetry-xray feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.GRPC)
                .language(language)
                .features(['tracing-opentelemetry-xray', 'kapt'])
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

    void 'for default application type test gradle tracing-opentelemetry-xray feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .features(['tracing-opentelemetry-xray', 'kapt'])
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

    void 'for #applicationType test gradle tracing-opentelemetry-xray feature for language=#language'(Language language,
                                                                                                        BuildTool buildTool,
                                                                                                        ApplicationType applicationType) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(applicationType)
                .language(language)
                .features(['tracing-opentelemetry-xray', 'kapt'])
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

    void 'test maven tracing-opentelemetry-xray feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['tracing-opentelemetry-xray'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry.contrib</groupId>
      <artifactId>opentelemetry-aws-xray</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-extension-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-exporter-otlp</artifactId>
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

    void 'for function test maven tracing-opentelemetry-xray feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .applicationType(ApplicationType.FUNCTION)
                .language(language)
                .features(['tracing-opentelemetry-xray'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry.contrib</groupId>
      <artifactId>opentelemetry-aws-xray</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-extension-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.opentelemetry</groupId>
      <artifactId>opentelemetry-exporter-otlp</artifactId>
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
