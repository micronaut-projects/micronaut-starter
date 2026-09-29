package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class ThymeleafSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-thymeleaf contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['views-thymeleaf'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://www.thymeleaf.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#thymeleaf")

        and:
        output.containsKey("src/main/resources/views/layout.html")
    }

    void 'test #buildTool views-thymeleaf feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['views-thymeleaf'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-thymeleaf", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.views", "micronaut-views-fieldset", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }

    }


    void 'test fieldset resources are generated for #buildTool views-thymeleaf feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language, buildTool), ['views-thymeleaf'])

        then: 'files are created in the language-specific views directory'
        String viewsPath = language == Language.PYTHON ? 'views' : 'src/main/resources/views'
        output.keySet().findAll { it.startsWith("${viewsPath}/fieldset") }

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }
    }
}
