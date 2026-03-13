package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class ConfigurationValidationSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    ConfigurationValidation configurationValidation = beanContext.getBean(ConfigurationValidation)

    void 'configuration-validation adds configurationValidation block'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                ['configuration-validation'])
        String pom = output['pom.xml']

        then:
        pom
        pom.contains("<configurationValidation>")
    }

    void 'configuration validation has expected metadata'() {
        expect:
        configurationValidation.name == 'configuration-validation'
        configurationValidation.title == 'Configuration Validation'
        configurationValidation.description == 'Validate Micronaut configuration wiring and values.'
    }

    void 'configuration validation title and description are different'() {
        expect:
        configurationValidation.title
        configurationValidation.description
        configurationValidation.title != configurationValidation.description
    }

    void 'configuration validation belongs to Validation category'() {
        expect:
        configurationValidation.category == Category.VALIDATION
    }

    @Unroll('feature configuration-validation supports application type #applicationType')
    void 'configuration validation supports all application types'(ApplicationType applicationType) {
        expect:
        configurationValidation.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }
}
