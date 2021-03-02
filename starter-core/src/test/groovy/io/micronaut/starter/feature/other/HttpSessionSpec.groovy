package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class HttpSessionSpec extends BeanContextSpec implements CommandOutputFixture {
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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['http-session'],
                language), new GradleBuild()).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-session")')

        where:
        language << Language.values().toList()

    }

    @Unroll
    void 'test http-session with Maven for language=#language'() {
        when:
        def context = buildGeneratorContext(
                ['http-session'],
                new Options(language, BuildTool.MAVEN), ApplicationType.DEFAULT)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(),
                context.getFeatures(), new MavenBuild([], [], context.getBuildProperties().getProperties())).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-session</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
