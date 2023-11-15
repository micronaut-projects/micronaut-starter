package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Subject

class JunitPlatformSuiteEngineSpec extends ApplicationContextSpec {

    @Subject
    JunitPlatformSuiteEngine feature = beanContext.getBean(JunitPlatformSuiteEngine)

    void 'junit-platform-suite-engine feature is visible'() {
        expect:
        feature.visible
    }

    void 'junit-platform-suite-engine feature is in the TEST category'() {
        expect:
        feature.category == Category.TEST
    }

    void "junit-platform-suite-engine supports #applicationType"(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'test #buildTool junit-platform-suite-engine feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .features([JunitPlatformSuiteEngine.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("org.junit.platform", "junit-platform-suite-engine", Scope.TEST)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}

