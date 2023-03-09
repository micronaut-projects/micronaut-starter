package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.dependencies.CoordinateResolver
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class GradleEnterpriseSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "if you add gradle-enterprise to a #buildTool build plugin is configured in settings"(BuildTool buildTool) {
        when:
        BuildBuilder builder = new BuildBuilder(beanContext, buildTool)
                    .language(Language.JAVA)
                    .applicationType(ApplicationType.DEFAULT)
                    .features(["gradle-enterprise"])
        Project project = builder.getProject()
        GradleBuild gradleBuild = (GradleBuild) builder.build(false)
        String settings = settingsGradle.template(project, gradleBuild, false, []).render().toString()
        String expectedVersion = builder.beanContext.getBean(CoordinateResolver).resolve(GradleEnterprise.GRADLE_ENTERPRISE_ARTIFACT_ID).get().version

        then:
        !settings.contains("allowUntrustedServer = true")
        !settings.contains("server =")
        settings.contains('gradleEnterprise {')
        settings.contains('buildScan {')
        settings.contains('termsOfServiceUrl = "https://gradle.com/terms-of-service"')
        settings.contains('termsOfServiceAgree = "yes"')
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert settings.contains($/id("com.gradle.enterprise") version("$expectedVersion")/$)
        } else if (buildTool == BuildTool.GRADLE) {
            assert settings.contains($/id "com.gradle.enterprise" version "$expectedVersion"/$)
        }

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }
}
