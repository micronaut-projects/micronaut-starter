package io.micronaut.starter.feature.picocli

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.Kapt
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class PicocliSpec extends ApplicationContextSpec {

    @Unroll
    void 'test cli app contains picocli-gen as annotation processor for buildTool=#buildTool language=#language'(Language language, BuildTool buildTool) {
        given:
        def features = language == Language.KOTLIN ? [Kapt.NAME] : []

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(features)
                .applicationType(ApplicationType.CLI)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("info.picocli", "picocli")
        verifier.hasDependency("io.micronaut.picocli", "micronaut-picocli")
        if (language == Language.KOTLIN && buildTool.isGradle()) {
            assert verifier.hasDependency("info.picocli", "picocli-codegen", "kapt")
        } else {
            assert verifier.hasAnnotationProcessor("info.picocli", "picocli-codegen")
        }

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void 'test maven cli app JAVA contains picocli-gen as annotation processor'() {
        when:
        Language language = Language.JAVA
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.JAVA)
                .applicationType(ApplicationType.CLI)
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)
        then:
        verifier.hasDependency("info.picocli", "picocli-codegen", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("info.picocli", "picocli-codegen", Scope.TEST_ANNOTATION_PROCESSOR)
        !verifier.hasDependency("io.micronaut", "micronaut-inject-java", Scope.ANNOTATION_PROCESSOR)
        !verifier.hasDependency("io.micronaut", "micronaut-inject-java", Scope.TEST_ANNOTATION_PROCESSOR)
        !verifier.hasDependency("io.micronaut.validation", "micronaut-validation-processor", Scope.ANNOTATION_PROCESSOR)
        !verifier.hasDependency("io.micronaut.validation", "micronaut-validation-processor", Scope.TEST_ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.picocli", "micronaut-picocli", Scope.COMPILE)

        and: 'property is not defined it is inherited via the bom'
        !parsePropertySemanticVersion(template, "picocli.version").isPresent()
    }

    void 'test maven cli app Kotlin contains picocli-gen as annotation processor'() {
        when:
        Language language = Language.KOTLIN
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(ApplicationType.CLI)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)
        then:
        verifier.hasDependency("info.picocli", "picocli-codegen", Scope.ANNOTATION_PROCESSOR)
        !verifier.hasDependency("info.picocli", "picocli-codegen", Scope.TEST_ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut", "micronaut-inject-java", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut", "micronaut-inject-java", Scope.TEST_ANNOTATION_PROCESSOR)
        !verifier.hasDependency("io.micronaut.validation", "micronaut-validation-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.picocli", "micronaut-picocli", Scope.COMPILE)

        and: 'property is not defined it is inherited via the bom'
        !parsePropertySemanticVersion(template, "picocli.version").isPresent()
    }

    void "test the test features are applied"() {
        when:
        Options options = new Options(Language.JAVA, null, BuildTool.GRADLE)
        Features features = getFeatures([], options, ApplicationType.CLI)
        GeneratorContext generatorContext = buildGeneratorContext([], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-junit")
        features.contains("junit")
        generatorContext.getTemplates().containsKey("picocliJunitTest")
        !features.contains(Kapt.NAME)
        !features.contains("picocli-spock")
        !features.contains("picocli-kotlintest")
        !generatorContext.getTemplates().containsKey("testDir")

        when:
        options = new Options(Language.GROOVY, null, BuildTool.GRADLE)
        features = getFeatures([], options, ApplicationType.CLI)
        generatorContext = buildGeneratorContext([], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-spock")
        features.contains("spock")
        generatorContext.getTemplates().containsKey("picocliSpock")
        !features.contains("picocli-junit")
        !features.contains("picocli-kotlintest")
        !generatorContext.getTemplates().containsKey("testDir")

        when:
        options = new Options(Language.KOTLIN, null, BuildTool.GRADLE)
        features = getFeatures([Kapt.NAME], options, ApplicationType.CLI)
        generatorContext = buildGeneratorContext([Kapt.NAME], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains(Kapt.NAME)
        features.contains("picocli-junit")
        features.contains("junit")
        generatorContext.getTemplates().containsKey("picocliJunitTest")
        !features.contains("picocli-kotlintest")
        !features.contains("picocli-spock")
        !generatorContext.getTemplates().containsKey("testDir")

        when:
        def kaptFeatures = language == Language.KOTLIN ? [Kapt.NAME] : []
        options = new Options(language, TestFramework.JUNIT, BuildTool.GRADLE)
        features = getFeatures(kaptFeatures, options, ApplicationType.CLI)
        generatorContext = buildGeneratorContext(kaptFeatures, options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-junit")
        features.contains("junit")
        generatorContext.getTemplates().containsKey("picocliJunitTest")
        !features.contains("picocli-spock")
        !features.contains("picocli-kotlintest")
        !generatorContext.getTemplates().containsKey("testDir")

        where:
        language << Language.values()
    }

    void "test that picocli with kotlin language requires kapt with buildTool=#buildTool"() {
        when:
        new BuildBuilder(beanContext, buildTool)
                .language(Language.KOTLIN)
                .applicationType(ApplicationType.CLI)
                .render()

        then:
        def ex = thrown(IllegalArgumentException)
        ex.getMessage() == 'Feature picocli-kotlin-application is incompatible with Kotlin KSP and requires Kapt instead.'

        when:
        new BuildBuilder(beanContext, buildTool)
                .language(Language.KOTLIN)
                .features([Kapt.NAME])
                .applicationType(ApplicationType.CLI)
                .render()

        then:
        noExceptionThrown()

        where: "BuildTool.MAVEN always includes Kapt"
        buildTool << BuildTool.valuesGradle()
    }
}
