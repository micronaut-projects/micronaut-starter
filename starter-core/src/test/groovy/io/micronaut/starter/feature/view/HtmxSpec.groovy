package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Unroll

class HtmxSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature htmx contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['htmx'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://htmx.org/')
        readme.contains('https://micronaut-projects.github.io/micronaut-views/latest/guide/#htmx')
    }

    @Unroll
    void 'test gradle htmx feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(["htmx", "views-thymeleaf"])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-htmx")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven htmx feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['htmx'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency('io.micronaut.views', 'micronaut-views-htmx', Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }
}
