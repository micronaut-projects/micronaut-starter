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

class OpenTelemetryHttpSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryHttp feature = beanContext.getBean(OpenTelemetryHttp)

    void 'tracing-opentelemetry-exporter-http feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-exporter-http feature is not visible'() {
        expect:
        !feature.isVisible()
    }

    @Unroll
    void 'feature tracing-opentelemetry-http does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    @Unroll
    void 'feature tracing-opentelemetry-http supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }

    void 'test gradle tracing-opentelemetry-http feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-http'])
                .render()

        then:
        template.contains('implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")')

        where:
        language << Language.values().toList()
    }

    void 'test maven tracing-opentelemetry-http feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['tracing-opentelemetry-http'])
                .render()

        then:
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
}
