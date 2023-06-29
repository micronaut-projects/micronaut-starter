package io.micronaut.starter.feature.rxjava

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class RxJava3HttpClientSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    RxJava3HttpClient rxjava3HttpClient = beanContext.getBean(RxJava3HttpClient)

    void "rxjava3-http-client supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        rxjava3HttpClient.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "rxjava3-http-client is visible"() {
        expect:
        rxjava3HttpClient.isVisible()
    }

    @Unroll
    void 'dependency is included with #buildTool and feature rxjava3 for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['rxjava3-http-client'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.rxjava3", "micronaut-rxjava3-http-client", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
