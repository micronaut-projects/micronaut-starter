package io.micronaut.starter.feature.lang.kotlin

import io.micronaut.core.version.SemanticVersion
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KotlinApplicationSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    KotlinApplication kotlinApplication = beanContext.getBean(KotlinApplication)

    void 'test KSP feature'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .features(["ksp", "security"])
                .build()
        when:
        Map<String, String> output = generate(options)
        String buildGradle = output[BuildTool.GRADLE.getBuildFileName()]

        then:
        output.containsKey("src/main/kotlin/example/micronaut/Application.${Language.KOTLIN.extension}".toString())
        buildGradle
        buildGradle.contains('id("org.jetbrains.kotlin.jvm")')
        !buildGradle.contains('kapt')
        buildGradle.contains('id("com.google.devtools.ksp")')
        buildGradle.contains('mainClass = "example.micronaut.ApplicationKt"')
        buildGradle.contains('implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")')
        buildGradle.contains("ksp(\"io.micronaut.security:micronaut-security-annotations\")")
    }

    @Unroll
    void 'Application file is generated for a default application type with #buildTool and language: kotlin and testing framework: #testFramework'(BuildTool buildTool, TestFramework testFramework) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .testFramework(testFramework)
                .buildTool(buildTool)
                .build()
        when:
        def output = generate(options)

        then:
        output.containsKey("src/main/kotlin/example/micronaut/Application.${Language.KOTLIN.extension}".toString())

        when:
        String buildGradle = output[buildTool.getBuildFileName()]
        String pom = output['pom.xml']
        String template = buildTool.isGradle() ? buildGradle : pom

        then:
        if (buildTool.isGradle()) {
            assert buildGradle
            assert buildGradle.contains('id("org.jetbrains.kotlin.jvm")')
            assert buildGradle.contains('id("com.google.devtools.ksp")')
            assert buildGradle.contains('mainClass = "example.micronaut.ApplicationKt"')
        }
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.kotlin", "micronaut-kotlin-runtime", Scope.COMPILE)
        verifier.hasDependency("com.fasterxml.jackson.module", "jackson-module-kotlin", Scope.RUNTIME)

        where:
        [buildTool, testFramework] << [BuildTool.values(), [TestFramework.KOTEST]].combinations()
    }

    @Unroll
    void "kotlin-application does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !kotlinApplication.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << [
                ApplicationType.CLI,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    @Unroll
    void "kotlin-application supports #description application type"() {
        expect:
        kotlinApplication.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values().toList() - [
                ApplicationType.CLI,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    void "test kotlin app with maven defines kotlinVersion build property"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.KOTLIN)
                .render()
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, 'kotlinVersion')

        then:
        semanticVersionOptional.isPresent()
    }

    void "test kotlin app gradle build plugins"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(Language.KOTLIN)
                .render()
        String pluginId = 'org.jetbrains.kotlin.jvm'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        semanticVersionOptional.isPresent()
    }
}
