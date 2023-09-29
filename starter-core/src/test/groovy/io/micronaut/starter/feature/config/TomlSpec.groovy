package io.micronaut.starter.feature.config

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.FeaturePhase
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Options
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
        }, new Options())
        def output = generate(ApplicationType.DEFAULT, generatorContext)

        then:
        output["src/main/resources/application.toml"].contains("micronaut.application.name = 'foo'\n")
        output["src/main/resources/bootstrap.toml"] == "abc = 123\n"
        output["src/test/resources/application-test.toml"] == "abc = 456\n"
        output["src/main/resources/application-prod.toml"] == "abc = 789\n"
        output["build.gradle.kts"].contains('io.micronaut.toml:micronaut-toml')
    }
}
