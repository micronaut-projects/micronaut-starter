package io.micronaut.starter.feature.config

import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.feature.FeaturePhase
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.options.Options
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class PropertiesSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Properties props = beanContext.getBean(Properties)

    void "order is highest"() {
        expect:
        props.order == FeaturePhase.HIGHEST.getOrder()
    }

    @Unroll
    void "properties supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        props.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "test configuration files generated for default properties feature"() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext([], { context ->
            context.getBootstrapConfiguration().put("abc", 123)
            context.getConfiguration("test", ApplicationConfiguration.testConfig()).put("abc", 456)
            context.getConfiguration("prod", new ApplicationConfiguration("prod")).put("abc", 789)
        }, MicronautOptions.builder().build())
        def output = generate(ApplicationType.DEFAULT, generatorContext)

        then:
        output["src/main/resources/application.properties"].readLines()[1] == 'micronaut.application.name=foo'
        output["src/main/resources/bootstrap.properties"].readLines()[1] == 'abc=123'
        output["src/test/resources/application-test.properties"].readLines()[1] == 'abc=456'
        output["src/main/resources/application-prod.properties"].readLines()[1] == 'abc=789'
    }
}
