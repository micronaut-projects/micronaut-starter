package io.micronaut.starter.feature.cache

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Unroll

class EHCacheSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature cache-ehcache contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['cache-ehcache'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html#ehcache")
        readme.contains("https://www.ehcache.org/")
    }

    @Unroll
    void 'test gradle cache-ehcache feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cache-ehcache'])
                .render()

        then:
        template.contains('implementation("io.micronaut.cache:micronaut-cache-ehcache")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven cache-ehcache feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cache-ehcache'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.cache", "micronaut-cache-ehcache", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test cache-ehcache configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cache-ehcache'])

        then:
        commandContext.configuration.get('micronaut.caches.my-cache.maximumSize'.toString()) == 20
    }

}
