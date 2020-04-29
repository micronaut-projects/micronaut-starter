package io.micronaut.starter.feature.stackdriver

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class StackdriverSpec extends BeanContextSpec {

    @Subject
    @Shared
    Stackdriver stackdriver = beanContext.getBean(Stackdriver)

    void "stackdriver belongs to Logging category"() {
        expect:
        Category.LOGGING == stackdriver.category
    }

    void "stackdriver is visible"() {
        expect:
        stackdriver.visible
    }

    void "stackdriver title and description are different"() {
        expect:
        stackdriver.getTitle()
        stackdriver.getDescription()
        stackdriver.getTitle() != stackdriver.getDescription()
    }

    @Unroll("feature stackdriver works for application type: #applicationType")
    void "feature stackdriver works for every type of application type"(ApplicationType applicationType) {
        expect:
        stackdriver.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature stackdriver for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['stackdriver'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-tracing</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature stackdriver for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['stackdriver'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.gcp:micronaut-gcp-tracing")')

        where:
        language << Language.values()
    }
}
