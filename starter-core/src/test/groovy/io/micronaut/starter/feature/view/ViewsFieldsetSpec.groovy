package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject
import spock.lang.Unroll

class ViewsFieldsetSpec extends ApplicationContextSpec {

    @Subject
    ViewsFieldset feature = beanContext.getBean(ViewsFieldset)

    void 'views-fieldset feature is visible'() {
        expect:
        feature.visible
    }

    void 'views-fieldset feature is in the VIEWS category'() {
        expect:
        feature.category == Category.VIEW
    }

    void "views-fieldset supports #applicationType"(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT]
    }

    void "views-fieldset does not support #applicationType"(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values() - ApplicationType.DEFAULT
    }

    @Unroll
    void 'test #buildTool views-fieldsets feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['views-fieldset'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-fieldset", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
