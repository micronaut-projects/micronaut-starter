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

class OpenTelemetryAnnotationsSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    OpenTelemetryAnnotations feature = beanContext.getBean(OpenTelemetryAnnotations)

    void 'tracing-opentelemetry-annotations feature is in the tracing category'() {
        expect:
        feature.category == Category.TRACING
    }

    void 'tracing-opentelemetry-annotations feature is not visible'() {
        expect:
        !feature.isVisible()
    }

    @Unroll
    void 'feature tracing-opentelemetry-annotations does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.CLI]
    }

    @Unroll
    void 'feature tracing-opentelemetry-annotations supports #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.CLI)
    }

    void 'test gradle tracing-opentelemetry-annotations feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['tracing-opentelemetry-annotations', 'kapt'])
                .render()

        then:
        if (language == Language.KOTLIN) {
            assert template.contains('kapt("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")')
        } else if (language == Language.GROOVY) {
            assert template.contains('compileOnly("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")')
        } else if (language == Language.JAVA) {
            assert template.contains('annotationProcessor("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")')
        } else {
            assert !template.contains('kapt("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")')
            assert !template.contains('annotationProcessor("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")')
        }

        where:
        language << Language.values().toList()
    }

    void 'test maven tracing-opentelemetry-annotations feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['tracing-opentelemetry-annotations'])
                .render()

        then:
        if (language == Language.KOTLIN) {
            assert template.contains('''
               <annotationProcessorPath>
                 <groupId>io.micronaut.tracing</groupId>
                 <artifactId>micronaut-tracing-opentelemetry-annotation</artifactId>
                 <version>${micronaut.tracing.version}</version>
               </annotationProcessorPath>''')
        } else if(language == Language.JAVA) {
            assert template.contains('''
            <path>
              <groupId>io.micronaut.tracing</groupId>
              <artifactId>micronaut-tracing-opentelemetry-annotation</artifactId>
              <version>${micronaut.tracing.version}</version>
              <exclusions>
                <exclusion>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject</artifactId>
                </exclusion>
              </exclusions>
            </path>''')
        } else if(language == Language.GROOVY) {
            template.contains('''
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry-annotation</artifactId>
      <scope>provided</scope>
    </dependency>
''')
        }
        where:
        language << Language.values().toList()
    }
}
