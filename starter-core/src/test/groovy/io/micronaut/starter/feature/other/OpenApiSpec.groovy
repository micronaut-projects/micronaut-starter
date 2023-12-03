package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class OpenApiSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature openapi contains links to micronaut docs'() {
        when:
        def output = generate(['openapi'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.openapis.org")
        readme.contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html")
    }

    void "openApi belongs to API category"() {
        expect:
        Category.API == beanContext.getBean(OpenApi).category
    }

    @Unroll
    void 'test swagger with #buildTool for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['openapi'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, TestFramework.JUNIT, template)

        then:
        verifier.hasAnnotationProcessor("io.micronaut.openapi", "micronaut-openapi")
        verifier.hasDependency("io.micronaut.openapi", "micronaut-openapi-annotations", Scope.COMPILE_ONLY)

        if (buildTool == BuildTool.MAVEN) {
            // property is not defined it is inherited via the bom
            assert !parsePropertySemanticVersion(template, "micronaut.openapi.version").isPresent()
        }

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
