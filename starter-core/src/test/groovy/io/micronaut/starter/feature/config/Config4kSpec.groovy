package io.micronaut.starter.feature.config

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.FeaturePhase
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class Config4kSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Config4k config4k = beanContext.getBean(Config4k)

    void "config4k only works with kotlin"() {
        expect:
        config4k.requiredLanguage == Language.KOTLIN
    }

    void "title of config4k is not null"() {
        expect:
        config4k.title
    }

    void "order is highest"() {
        expect:
        config4k.order == FeaturePhase.HIGHEST.getOrder()
    }

    @Unroll
    void "config4k supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        config4k.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "test configuration files generated for config4k feature"() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext(['config4k'], {context ->
            context.getBootstrapConfiguration().put("abc", 123)
            context.getConfiguration("test", ApplicationConfiguration.testConfig()).put("abc", 456)
            context.getConfiguration("prod", new ApplicationConfiguration("prod")).put("abc", 789)
        }, new Options(Language.KOTLIN, null, BuildTool.GRADLE))
        def output = generate(ApplicationType.DEFAULT, generatorContext)

        then:
        output["src/main/resources/application.conf"].contains '''\
micronaut {
    application {
        name=foo
    }
}
'''
        output["src/main/resources/bootstrap.conf"] == '''\
abc=123
'''
        output["src/test/resources/application-test.conf"] == '''\
abc=456
'''
        output["src/main/resources/application-prod.conf"] == '''\
abc=789
'''
    }
}
