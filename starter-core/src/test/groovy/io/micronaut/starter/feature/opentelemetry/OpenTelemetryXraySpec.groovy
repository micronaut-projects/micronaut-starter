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
        "tracecontext, baggage, xray" == ctx.configuration.get('otel.traces.propagator')
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
        where:
        language << Language.values().toList()
    }
}
