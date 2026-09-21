package io.micronaut.starter.feature.testresources

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class TestResourcesSpec extends ApplicationContextSpec {

    void 'test maven testresources generates dependency when language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestResources.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.testresources", "micronaut-test-resources-client", Scope.COMPILE_ONLY)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test pyronaut testresources generates dependency'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.PYRONAUT)
                .language(Language.PYTHON)
                .testFramework(TestFramework.PYTEST)
                .features([TestResources.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.PYRONAUT, Language.PYTHON, template)

        then:
        verifier.hasDependency("io.micronaut.testresources", "micronaut-test-resources-client")
        template.contains("[tool.pyronaut.test-resources]")
        template.contains("enabled = true")
    }
}
