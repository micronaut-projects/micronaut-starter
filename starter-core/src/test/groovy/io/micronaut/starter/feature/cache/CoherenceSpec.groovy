package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature cache-coherence contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([Coherence.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#cache")
        readme.contains("https://coherence.java.net/")
    }

    @Unroll
    void 'test gradle cache-coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([Coherence.NAME])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-cache")')
        template.contains('implementation("com.oracle.coherence.ce:coherence")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven cache-coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([Coherence.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.coherence", "micronaut-coherence", Scope.COMPILE)
        verifier.hasDependency("com.oracle.coherence.ce", "coherence", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.coherence", "micronaut-coherence-cache", Scope.COMPILE)
        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }
}
