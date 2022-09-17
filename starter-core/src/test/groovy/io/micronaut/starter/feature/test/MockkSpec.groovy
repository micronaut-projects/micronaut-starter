package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class MockkSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    Mockk mockk = beanContext.getBean(Mockk)

    void 'test readme.md with feature mockk contains links to 3rd party docs'() {
        when:
        Options options = new Options(Language.KOTLIN, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11, Collections.emptyMap())
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, ['mockk'])
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
        Set<Feature> features = [mockk] as Set<Feature>
        expect:
        !mockk.shouldApply(ApplicationType.DEFAULT, new Options(language, TestFramework.KOTEST, BuildTool.MAVEN), features)

        where:
        language << Language.values() - Language.KOTLIN
    }

    @Unroll
    void 'test mockk feature is added automatically for Maven and Kotest for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(JdkVersion.JDK_11)
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