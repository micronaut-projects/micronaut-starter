package io.micronaut.starter.feature.rxjava

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.reactor.ReactorHttpClient
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class RxJava2HttpClientSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    ReactorHttpClient rxjava2HttpClient = beanContext.getBean(ReactorHttpClient)

    void "rxjava2-http-client supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        rxjava2HttpClient.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "rxjava2-http-client is visible"() {
        expect:
        rxjava2HttpClient.isVisible()
    }

    @Unroll
    void 'dependency is included with #buildTool and feature rxjava2 for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['rxjava2-http-client'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.rxjava2", "micronaut-rxjava2-http-client", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
