package io.micronaut.starter.feature.netflix

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Unroll

class ArchaiusSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle netflix-archaius feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['netflix-archaius'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.netflix:micronaut-netflix-archaius")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven netflix-archaius feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['netflix-archaius'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.netflix", "micronaut-netflix-archaius", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

}
