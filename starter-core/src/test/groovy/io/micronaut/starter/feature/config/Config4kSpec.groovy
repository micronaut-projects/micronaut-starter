package io.micronaut.starter.feature.config

import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.feature.FeaturePhase
import io.micronaut.starter.feature.ci.workflows.oci.OCICiWorkflowFeature
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
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
        config4k.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "test configuration files generated for config4k feature"() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .buildTool(BuildTool.GRADLE)
                .build()

        GeneratorContext generatorContext = buildGeneratorContext(['config4k'], {context ->
            context.getBootstrapConfiguration().put("abc", 123)
            context.getConfiguration("test", ApplicationConfiguration.testConfig()).put("abc", 456)
            context.getConfiguration("prod", new ApplicationConfiguration("prod")).put("abc", 789)
        }, options)
        def output = generate( generatorContext)

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
