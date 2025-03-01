package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import spock.lang.Subject

class ViewsFieldsetTckSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    ViewsFieldsetTck feature = beanContext.getBean(ViewsFieldsetTck)

    void 'views-fieldset feature is visible'() {
        expect:
        !feature.visible
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

    void 'test #buildTool views-fieldsets feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([ViewsFieldsetTck.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-fieldset-tck", Scope.TEST)

        and:
        if (language != Language.GROOVY) {
            assert verifier.hasDependency("org.junit.platform", "junit-platform-suite-engine", Scope.TEST)
        }

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void 'ThymeleafSuite test is generated views-fieldset-tck'() {
        when:
        Map<String, String> output = generate([ViewsFieldsetTck.NAME])

        then:
        output.containsKey("${Language.DEFAULT_OPTION.testSrcDir}/example/micronaut/ThymeleafSuite.$Language.DEFAULT_OPTION.extension".toString())
    }
}
