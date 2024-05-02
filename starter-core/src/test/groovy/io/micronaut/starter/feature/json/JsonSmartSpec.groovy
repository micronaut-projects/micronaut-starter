package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared

class JsonSmartSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    JsonSmart jsonSmart = beanContext.getBean(JsonSmart)

    void 'test readme.md with feature json-smart contains links to third party docs'() {
        when:
        Map<String, String> output = generate([JsonSmart.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://netplex.github.io/json-smart/')
    }

    void "feature json-smart supports applicationType=#applicationType"(ApplicationType applicationType) {
        expect:
        jsonSmart.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "feature json-smart feature is TEST category"() {
        expect:
        jsonSmart.category == Category.TEST
    }

    void "dependencies are present for #buildTool and #language"(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([JsonSmart.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("net.minidev", "json-smart", Scope.TEST)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }
}
