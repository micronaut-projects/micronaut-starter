package io.micronaut.starter.feature.rss

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

class RssSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature rss contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['rss'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rss/latest/guide/index.html#whatsNew")
    }

    @Unroll
    void 'test gradle rss feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['rss'])
                .render()

        then:
        template.contains('implementation("io.micronaut.rss:micronaut-rss")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven rss feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['rss'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.rss", "micronaut-rss", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

}
