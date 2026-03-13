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

class DependencyInjectionValidationSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    DependencyInjectionValidation dependencyInjectionValidation = beanContext.getBean(DependencyInjectionValidation)

    void 'dependency-injection-validation adds configurationValidation block'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                ['dependency-injection-validation'])
        String pom = output['pom.xml']

        then:
        pom
        pom.contains("<configurationValidation>")
        pom.contains("<validateDependencyInjection>true</validateDependencyInjection>")
    }

    void 'dependency injection validation has expected metadata'() {
        expect:
        dependencyInjectionValidation.name == 'dependency-injection-validation'
        dependencyInjectionValidation.title == 'Dependency Injection Validation'
        dependencyInjectionValidation.description == 'Validate Micronaut dependency injection wiring (missing beans, unsatisfied injections, etc.) '
    }

    void 'dependency injection validation title and description are different'() {
        expect:
        dependencyInjectionValidation.title
        dependencyInjectionValidation.description
        dependencyInjectionValidation.title != dependencyInjectionValidation.description
    }

    void 'dependency injection validation belongs to Validation category'() {
        expect:
        dependencyInjectionValidation.category == Category.VALIDATION
    }

    @Unroll('feature dependency-injection-validation supports application type #applicationType')
    void 'dependency injection validation supports all application types'(ApplicationType applicationType) {
        expect:
        dependencyInjectionValidation.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }
}
