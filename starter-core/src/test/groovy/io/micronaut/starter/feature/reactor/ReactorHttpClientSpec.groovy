package io.micronaut.starter.feature.reactor

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

class ReactorHttpClientSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    ReactorHttpClient reactorHttpClient = beanContext.getBean(ReactorHttpClient)

    void "reactor-http-client supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        reactorHttpClient.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "reactor-http-client is visible"() {
        expect:
        reactorHttpClient.isVisible()
    }

    @Unroll
    void 'dependency is included with #buildTool and feature reactor for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['reactor-http-client'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.reactor", "micronaut-reactor-http-client", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
