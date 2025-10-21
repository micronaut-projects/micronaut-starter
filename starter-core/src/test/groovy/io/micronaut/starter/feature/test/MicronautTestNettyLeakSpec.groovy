package io.micronaut.starter.feature.test

import io.micronaut.core.util.StringUtils
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class MicronautTestNettyLeakSpec extends ApplicationContextSpec implements CommandOutputFixture {

    static final String FEATURE = "test-netty-leak"

    @Shared
    @Subject
    MicronautTestNettyLeak featureSubject = beanContext.getBean(MicronautTestNettyLeak)

    void 'test readme.md with feature test-netty-leak contains links to docs'() {
        when:
        Map<String, String> output = generate([FEATURE])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://netty.io/4.2/api/io/netty/util/LeakPresenceDetector.html")
        readme.contains("https://micronaut-projects.github.io/micronaut-test/latest/guide/#nettyLeak")
    }

    void "test test-netty-leak belongs to Test category"() {
        expect:
        Category.TEST == featureSubject.category
    }

    @Unroll
    void 'test gradle test-netty-leak feature for language: #language buildTool: #buildTool testFramework: #testFramework and feature: #featureName'(
            String featureName,
            Language language,
            BuildTool buildTool,
            TestFramework testFramework) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(StringUtils.isEmpty(featureName) ? [] : [featureName])
                .language(language)
                .testFramework(testFramework)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, testFramework, template)

        then:
        verifier.hasDependency("io.micronaut.test", "micronaut-test-netty-leak", Scope.TEST_RUNTIME)

        where:
        [featureName, language, buildTool, testFramework] << [
                [FEATURE],
                Language.values().toList(),
                BuildTool.values().toList(),
                [TestFramework.JUNIT, TestFramework.SPOCK]
        ].combinations()
    }

    @Unroll
    void 'test gradle test-netty-leak does not add feature if server is tomcat'() {
        given:
        List<String> features = [FEATURE, 'tomcat-server']
        Language language = Language.JAVA
        BuildTool buildTool = BuildTool.GRADLE_KOTLIN
        TestFramework testFramework = TestFramework.JUNIT

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .testFramework(testFramework)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, testFramework, template)

        then:
        !verifier.hasDependency("io.micronaut.test", "micronaut-test-netty-leak", Scope.TEST_RUNTIME)
    }

    @Unroll
    void 'test gradle test-netty-leak is not added automatically if test framework is KoTest'() {
        given:
        List<String> features = []
        Language language = Language.JAVA
        BuildTool buildTool = BuildTool.GRADLE_KOTLIN
        TestFramework testFramework = TestFramework.KOTEST

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .testFramework(testFramework)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, testFramework, template)

        then:
        !verifier.hasDependency("io.micronaut.test", "micronaut-test-netty-leak", Scope.TEST_RUNTIME)
    }

    @Unroll
    void 'test gradle test-netty-leak adds junit.jupiter.extensions.autodetection.enabled'() {
        given:
        Language language = Language.JAVA

        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(language))
        String junitPlatformProperties = output["src/test/resources/junit-platform.properties"]

        then:
        junitPlatformProperties
        junitPlatformProperties.contains('junit.jupiter.extensions.autodetection.enabled=true')
    }
}