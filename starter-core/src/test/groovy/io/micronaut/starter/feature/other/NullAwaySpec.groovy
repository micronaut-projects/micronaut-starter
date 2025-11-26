package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.BuildToolSpec
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

class NullAwaySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    NullAway nullAway = beanContext.getBean(NullAway)

    void "nullaway supports application type #appType"(ApplicationType appType) {
        expect:
        nullAway.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void 'test README.md with feature nullaway contains links to docs'() {
        when:
        def output = generate(['null-away'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/uber/NullAway/wiki")
    }

    void "nullaway belongs to Validation category"() {
        expect:
        Category.VALIDATION == beanContext.getBean(NullAway).category
    }

    void 'test Gradle nullaway feature dependencies'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["null-away"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(GRADLE, template)

        then:
        verifier.hasDependency("org.jspecify", "jspecify", Scope.COMPILE)
        verifier.hasDependency("com.uber.nullaway", "nullaway", "errorprone")
        verifier.hasDependency("com.google.errorprone", "error_prone_core", "errorprone")
        verifier.hasBuildPlugin("net.ltgt.errorprone")

        template.contains('check("NullAway", net.ltgt.gradle.errorprone.CheckSeverity.ERROR)')

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void 'test Maven nullaway feature dependencies'() {
        when:
        String template = new BuildBuilder(beanContext, MAVEN)
                .features(["null-away"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(MAVEN, template)

        then:
        verifier.hasDependency("org.jspecify", "jspecify", Scope.COMPILE)
        verifier.hasAnnotationProcessor("com.uber.nullaway", "nullaway")
        verifier.hasAnnotationProcessor("com.google.errorprone", "error_prone_core")
    }

    void 'test maven jvm config'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                ['null-away'])
        def maven = output['.mvn/jvm.config']

        then:
        maven
        maven.contains("--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED")
    }

    void 'test nullaway maven args'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                ['null-away'])
        def pom = output['pom.xml']

        then:
        pom
        pom.contains("<arg>-XDcompilePolicy=simple</arg>")
    }
}
