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

class InfinispanSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature cache-infinispan contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['cache-infinispan'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html#infinispan")
        readme.contains("https://infinispan.org")
    }

    @Unroll
    void 'test gradle cache-infinispan feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cache-infinispan'])
                .render()

        then:
        template.contains('implementation("io.micronaut.cache:micronaut-cache-infinispan")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven cache-infinispan feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cache-infinispan'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.cache", "micronaut-cache-infinispan", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test cache-infinispan configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cache-infinispan'])

        then:
        commandContext.configuration.get('infinispan.client.hotrod.server.host'.toString()) == 'infinispan.example.com'
        commandContext.configuration.get('infinispan.client.hotrod.server.port'.toString()) == 10222
    }

}
