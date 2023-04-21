package io.micronaut.starter.feature.lang.kotlin

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KotlinApplicationSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    KotlinApplication kotlinApplication = beanContext.getBean(KotlinApplication)

    @Unroll
    void 'Application file is generated for a default application type with #buildTool and language: kotlin and testing framework: #testFramework'(BuildTool buildTool, TestFramework testFramework) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, testFramework, buildTool),
                []
        )

        then:
        output.containsKey("src/main/kotlin/example/micronaut/Application.${Language.KOTLIN.extension}".toString())

        when:
        String buildGradle = output[buildTool.getBuildFileName()]
        String pom = output['pom.xml']
        String template = buildTool.isGradle() ? buildGradle : pom

        then:
        if (buildTool.isGradle()) {
            assert buildGradle
            assert buildGradle.contains('mainClass.set("example.micronaut.ApplicationKt")')
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
        !kotlinApplication.supports(applicationType)

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
        kotlinApplication.supports(applicationType)

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
