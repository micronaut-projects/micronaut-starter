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

class MicronautGradleEnterpriseSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "if you add gradle-enterprise plugin is configured in settings.gradle"(BuildTool buildTool) {
        when:
        BuildBuilder builder = new BuildBuilder(beanContext, buildTool)
                    .language(Language.JAVA)
                    .applicationType(ApplicationType.DEFAULT)
                    .features(["micronaut-gradle-enterprise"])
        Project project = builder.getProject()
        GradleBuild gradleBuild = (GradleBuild) builder.build(false)
        String settings = settingsGradle.template(project, gradleBuild).render().toString()

        then:
        !settings.contains("allowUntrustedServer = true")
        settings.contains('gradleEnterprise {')
        settings.contains('server = "https://ge.micronaut.io"')
        settings.contains('buildScan {')
        settings.contains('termsOfServiceUrl = "https://gradle.com/terms-of-service"')
        !settings.contains('termsOfServiceAgree = "yes"')
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert settings.contains('id("com.gradle.enterprise") version("3.8.1")')
        } else if (buildTool == BuildTool.GRADLE) {
            assert settings.contains('id "com.gradle.enterprise" version "3.8.1"')
        }

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void "MicronautGradleEnterprise is not visible"() {
        expect:
        !beanContext.getBean(MicronautGradleEnterprise).isVisible()
    }
}
