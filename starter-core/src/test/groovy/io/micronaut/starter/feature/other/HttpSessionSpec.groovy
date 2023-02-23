package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
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
        def output = generate(['http-session'])
        def readme = output["README.md"]

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
    void 'test http-session with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['http-session'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.session:micronaut-session")')

        where:
        language << Language.values().toList()

    }

    @Unroll
    void 'test http-session with Maven for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['http-session'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.session</groupId>
      <artifactId>micronaut-session</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
