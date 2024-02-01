package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class HttpSessionSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    HttpSession httpSession = beanContext.getBean(HttpSession)

    void 'test readme.md with feature http-session contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['http-session'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#sessions")
    }

    void 'test http-session configurations (with and without redis)'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['http-session'])

        then:
        commandContext.configuration.get('micronaut.session.http.cookie'.toString()) == true
        commandContext.configuration.get('micronaut.session.http.header'.toString()) == true
        !commandContext.configuration.get('micronaut.session.http.redis.enabled'.toString())

        when:
        commandContext = buildGeneratorContext(['http-session','redis-lettuce'])

        then:
        commandContext.configuration.get('micronaut.session.http.cookie'.toString()) == true
        commandContext.configuration.get('micronaut.session.http.header'.toString()) == true
        commandContext.configuration.get('micronaut.session.http.redis.enabled'.toString()) == true
    }

    void "test http-session belongs to Client category"() {
        expect:
        Category.CLIENT == httpSession.category
    }

    @Unroll
    void 'test http-session with Gradle for language=#language and buildTool=#buildTool'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['http-session'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.session", "micronaut-session", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()

    }
}
