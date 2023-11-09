package io.micronaut.starter.feature.buildless

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.feature.build.MicronautGradleEnterprise
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject

class BuildlessSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    Buildless buildless = beanContext.getBean(Buildless.class)

    @Override
    Map<String, Object> getConfiguration() {
        ["spec.name": "BuildlessSpec"]
    }

    void "if you add the buildless feature it is configured for #buildTool"() {
        given:when:
        BuildBuilder builder = new BuildBuilder(beanContext, buildTool)
                    .language(Language.JAVA)
                    .applicationType(ApplicationType.DEFAULT)
                    .features(["buildless"])
        Project project = builder.getProject()
        GradleBuild gradleBuild = (GradleBuild) builder.build(false)
        String settings = settingsGradle.template(project, gradleBuild, false, []).render().toString()

        then: 'we we should have a plugins block'
        settings.contains('plugins {')

        and: 'buildless defaults should enable the plugin'
        assert buildless.isUseCustomCachePlugin()
        assert buildless.isEnableRemoteCache()
        assert buildless.isRemoteCachePushEnabled()
        assert buildless.isUseExpectContinue()
        assert buildless.getRemoteCacheType() != null
        assert buildless.getRemoteCacheUri() != null

        and: 'buildless imports and block should be added'
        assert settings.contains("import build.less.plugin.settings.buildless")
        assert settings.contains("buildless {")

        and: 'buildless plugin should be added'
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert settings.contains('    id("build.less")')
        } else {
            assert settings.contains('    id "build.less"')
        }

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "io.micronaut.starter.feature.buildless.Buildless is visible"() {
        expect:
        beanContext.getBean(Buildless).isVisible()
    }
}
