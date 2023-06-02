package io.micronaut.starter.feature.opentelemetry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
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

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry-grpc</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        where:
        language << Language.values().toList()
    }
}
