package io.micronaut.starter.feature.config

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.FeaturePhase
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Options
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class YamlSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Yaml yaml = beanContext.getBean(Yaml)

    void "order is highest"() {
        expect:
        yaml.order == FeaturePhase.HIGHEST.getOrder()
    }

    @Unroll
    void "yaml supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        yaml.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "test configuration files generated for yaml feature"() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext([], { context ->
            context.getBootstrapConfiguration().put("abc", 123)
            context.getConfiguration("test", new ApplicationConfiguration("test", "test")).put("abc", 456)
            context.getConfiguration("prod", new ApplicationConfiguration("prod")).put("abc", 789)
        }, new Options())
        def output = generate(ApplicationType.DEFAULT, generatorContext)

        then:
        output["src/main/resources/application.yml"].contains '''\
micronaut:
  application:
    name: foo
'''
        output["src/main/resources/bootstrap.yml"] == '''\
abc: 123
'''
        output["src/test/resources/application-test.yml"] == '''\
abc: 456
'''
        output["src/main/resources/application-prod.yml"] == '''\
abc: 789
'''
    }

    void "test dependency added for yaml feature"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options())

        then:
        output["build.gradle"].contains('implementation("org.yaml:snakeyaml"')
    }
}
