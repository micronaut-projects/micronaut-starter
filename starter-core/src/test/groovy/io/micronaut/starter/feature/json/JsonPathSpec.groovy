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

class JsonPathSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    JsonPath jsonPath = beanContext.getBean(JsonPath)

    void 'test readme.md with feature json-path contains links to third party docs'() {
        when:
        Map<String, String> output = generate([JsonPath.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/json-path/JsonPath")
    }

    void "feature json-path supports applicationType=#applicationType"(ApplicationType applicationType) {
        expect:
        jsonPath.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "feature json-path feature is TEST category"() {
        expect:
        jsonPath.category == Category.TEST
    }

    void "dependencies are present for #buildTool and #language"(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([JsonPath.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("com.jayway.jsonpath", "json-path", Scope.TEST)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }
}
