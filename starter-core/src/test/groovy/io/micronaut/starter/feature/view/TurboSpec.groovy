package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class TurboSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-turbo contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['turbo'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://turbo.hotwired.dev/')
        readme.contains('https://micronaut-projects.github.io/micronaut-views/latest/guide/#turbo')
    }

    @Unroll
    void 'test gradle turbo feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['turbo', "views-thymeleaf"])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-turbo")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven turbo feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['turbo'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency('io.micronaut.views', 'micronaut-views-turbo', Scope.COMPILE)

        where:
        language << Language.values().toList()
    }
}
