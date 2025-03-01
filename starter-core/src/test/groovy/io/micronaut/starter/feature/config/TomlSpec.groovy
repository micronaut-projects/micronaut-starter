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

class TomlSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Toml toml = beanContext.getBean(Toml)

    void "order is highest"() {
        expect:
        toml.order == FeaturePhase.HIGHEST.getOrder()
    }

    void "test configuration files generated for properties feature"() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext([Toml.NAME], { context ->
            context.getBootstrapConfiguration().put("abc", 123)
            context.getConfiguration("test", ApplicationConfiguration.testConfig()).put("abc", 456)
            context.getConfiguration("prod", new ApplicationConfiguration("prod")).put("abc", 789)
        }, MicronautOptions.builder().build())
        def output = generate(ApplicationType.DEFAULT, generatorContext)

        then:
        output["src/main/resources/application.toml"].contains("micronaut.application.name = 'foo'\n")
        output["src/main/resources/bootstrap.toml"] == "abc = 123\n"
        output["src/test/resources/application-test.toml"] == "abc = 456\n"
        output["src/main/resources/application-prod.toml"] == "abc = 789\n"
        output["build.gradle.kts"].contains('io.micronaut.toml:micronaut-toml')
    }
}
