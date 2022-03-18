package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties

class MicronautGradleEnterpriseSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @RestoreSystemProperties
    @Unroll
    void "if you add micronaut-gradle-enterprise is configured in settings.gradle"(BuildTool buildTool) {
        given:when:
        BuildBuilder builder = new BuildBuilder(beanContext, buildTool)
                    .language(Language.JAVA)
                    .applicationType(ApplicationType.DEFAULT)
                    .features(["micronaut-gradle-enterprise"])
        Project project = builder.getProject()
        GradleBuild gradleBuild = (GradleBuild) builder.build(false)
        String settings = settingsGradle.template(project, gradleBuild).render().toString()

        then:
        settings.contains('pluginManagement {')
        settings.contains('    repositories {')
        settings.contains('        gradlePluginPortal()')
        settings.contains('        mavenCentral()')
        settings.contains('    }')
        settings.contains('}')
        settings.contains('plugins {')
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert settings.contains('    id("io.micronaut.build.internal.gradle-enterprise") version("')
        } else if (buildTool == BuildTool.GRADLE) {
            assert settings.contains('    id "io.micronaut.build.internal.gradle-enterprise" version "')
        }
        settings.contains('}')

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void "io.micronaut.starter.feature.build.gradle.MicronautGradleEnterprise is not visible"() {
        expect:
        !beanContext.getBean(MicronautGradleEnterprise).isVisible()
    }
}
