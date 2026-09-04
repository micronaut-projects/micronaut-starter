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

class HandlebarsSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature views-handlebars contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['views-handlebars'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://jknack.github.io/handlebars.java")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#handlebars")
    }

    @Unroll
    void 'test gradle views-handlebars feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['views-handlebars'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-handlebars")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven views-handlebars feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-handlebars'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-handlebars", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

}
