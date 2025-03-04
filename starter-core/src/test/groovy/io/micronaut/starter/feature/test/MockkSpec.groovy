package io.micronaut.starter.feature.test

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.projectgen.core.feature.Feature
import io.micronaut.projectgen.core.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class MockkSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    Mockk mockk = beanContext.getBean(Mockk)

    void 'test readme.md with feature mockk contains links to 3rd party docs'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features(['mockk'])
                .build()
        when:
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://mockk.io")
    }

    void "test mockk belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == mockk.category
    }

    @Unroll
    void 'mockk feature should not be applied for languages other than Kotlin'(Language language) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .testFramework(TestFramework.KOTEST)
                .buildTool(BuildTool.MAVEN)
                .build()
        Set<Feature> features = [mockk] as Set<Feature>
        expect:
        !mockk.shouldApply(options, features)

        where:
        language << Language.values() - Language.KOTLIN
    }

    @Unroll
    void 'test mockk feature is added automatically for Maven and Kotest for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .language(language)
                .features([])
                .testFramework(TestFramework.KOTEST)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.mockk</groupId>
      <artifactId>mockk</artifactId>
""")

        where:
        language << [Language.KOTLIN]
    }
}