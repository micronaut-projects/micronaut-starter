package io.micronaut.starter.feature.config

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.FeaturePhase
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class Config4kSpec  extends BeanContextSpec implements CommandOutputFixture {

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

    @Unroll
    void 'application.conf is generated for config4k feature and #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, buildTool),
                ['config4k']
        )

        then:
        output.containsKey("src/main/resources/application.conf")
        !output.containsKey("src/main/resources/application.yaml")
        !output.containsKey("src/main/resources/application.properties")

        and:
        output["src/main/resources/application.conf"] == '''\
micronaut {
    application {
        name=foo
    }
}
'''

        where:
        buildTool << BuildTool.values()
    }
}
