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

class MicronautBuildCacheSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @RestoreSystemProperties
    @Unroll
    void "if you add micronaut-build-cache plugin is configured in settings.gradle"(BuildTool buildTool) {
        given:
        System.setProperty("ge.cache.username", "sherlock")
        System.setProperty("ge.cache.password", "elementary")
        when:
        BuildBuilder builder = new BuildBuilder(beanContext, buildTool)
                    .language(Language.JAVA)
                    .applicationType(ApplicationType.DEFAULT)
                    .features(["micronaut-build-cache"])
        Project project = builder.getProject()
        GradleBuild gradleBuild = (GradleBuild) builder.build(false)
        String settings = settingsGradle.template(project, gradleBuild).render().toString()

        then:
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert settings.contains('remote<HttpBuildCache> {')
            assert settings.contains('url = uri("https://ge.micronaut.io/cache/")')
            assert settings.contains('isPush = true')
            assert settings.contains('credentials {')
            assert settings.contains('username = "sherlock"')
            assert settings.contains('password = "elementary"')
        } else if (buildTool == BuildTool.GRADLE) {
            assert settings.contains('remote(HttpBuildCache) {')
            assert settings.contains('url = "https://ge.micronaut.io/cache/"')
            assert settings.contains('push = true')
            assert settings.contains('credentials {')
            assert settings.contains('username = "sherlock"')
            assert settings.contains('password = "elementary"')
        }

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void "MicronautBuildCache is not visible"() {
        expect:
        !beanContext.getBean(MicronautBuildCache).isVisible()
    }
}
