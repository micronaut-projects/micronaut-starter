package io.micronaut.starter.feature.kotlin

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.LanguageSpecificFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KtorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    Ktor ktor = beanContext.getBean(Ktor)

    void 'test readme.md with feature ktor contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.KOTLIN, TestFramework.JUNIT, BuildTool.GRADLE)
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [Ktor.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kotlin/latest/guide/index.html#ktor")
    }

    void "ktor belongs to Logging category"() {
        expect:
        Category.SERVER == ktor.category
    }

    void "ktor requires kotlin"() {
        expect:
        ktor instanceof LanguageSpecificFeature
        ktor.getRequiredLanguage() == Language.KOTLIN
    }

    void "ktor is visible"() {
        expect:
        ktor.visible
    }

    void "ktor title and description are different"() {
        expect:
        ktor.getTitle()
        ktor.getDescription()
        ktor.getTitle() != ktor.getDescription()
    }

    @Unroll
    void "feature ktor does not support type: #applicationType"(ApplicationType applicationType) {
        expect:
        !ktor.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.DEFAULT)
    }

    @Unroll
    void "feature ktor supports #applicationType"(ApplicationType applicationType) {
        expect:
        ktor.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT]
    }

    @Unroll
    void 'dependency is included with #buildTool and feature ktor for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([Ktor.NAME])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.kotlin", "micronaut-ktor", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.kotlin", "micronaut-kotlin-runtime", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE)

        verifier.hasDependency("io.ktor", "ktor-server-netty-jvm", Scope.COMPILE)
        verifier.hasDependency("io.ktor", "ktor-serialization-jackson-jvm", Scope.COMPILE)
        verifier.hasDependency("io.ktor", "ktor-server-content-negotiation-jvm", Scope.COMPILE)

        !verifier.hasDependency("io.micronaut", "micronaut-http-server-netty", Scope.COMPILE)

        where:
        [language, buildTool] << [supportedLanguages(), BuildTool.values()].combinations()
    }

    @Unroll
    void 'exception for maven and feature ktor for language=#language'(Language language) {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([Ktor.NAME])
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("The selected features are incompatible")

        where:
        language << (Language.values().toList() - supportedLanguages())
    }

    @Unroll
    void 'dependency is included with gradle and feature ktor for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([Ktor.NAME])
                .language(language)
                .render()

        then:
        template.contains("mainClass.set(\"example.micronaut.Application\")")

        where:
        language << supportedLanguages()
    }

    @Unroll
    void 'exception with gradle and feature ktor for language=#language'(Language language) {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([Ktor.NAME])
                .language(language)
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("The selected features are incompatible")

        where:
        language << (Language.values().toList() - supportedLanguages())
    }

    @Unroll
    void 'sample route, feature and singletons are generated for ktor feature'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, BuildTool.MAVEN),
                [Ktor.NAME]
        )

        then:
        output.containsKey("$srcDir/example/micronaut/HomeRoute.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/JacksonFeature.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Application.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/NameTransformer.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/UppercaseTransformer.$extension".toString())

        where:
        language << supportedLanguages()
        extension << supportedLanguages().extension
        srcDir << supportedLanguages().srcDir
        testSrcDir << supportedLanguages().testSrcDir
    }

    private static List<Language> supportedLanguages() {
        [Language.KOTLIN]
    }
}
