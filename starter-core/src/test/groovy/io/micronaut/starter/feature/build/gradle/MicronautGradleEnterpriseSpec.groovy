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
import io.micronaut.starter.options.Options
import spock.lang.Unroll
import spock.util.environment.RestoreSystemProperties

class MicronautGradleEnterpriseSpec extends ApplicationContextSpec implements CommandOutputFixture {

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

    void 'feature micronaut-gradle-enterprise creates a .mvn/extensions dot xml file'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ["micronaut-gradle-enterprise"])

        String extensionsXml = output[".mvn/extensions.xml"]

        then:
        extensionsXml
        extensionsXml.count('<extensions>') == 1
        extensionsXml.count('</extensions>') == 1
        extensionsXml.count('<extension>') == 2
        extensionsXml.count('</extension>') == 2
        extensionsXml.count('<groupId>com.gradle</groupId>') == 2
        extensionsXml.count('<artifactId>gradle-enterprise-maven-extension</artifactId>') == 1
        extensionsXml.count('<artifactId>common-custom-user-data-maven-extension</artifactId>') == 1
        extensionsXml.count('<version>') == 2
        extensionsXml.count('</version>') == 2
    }

    void 'feature micronaut-gradle-enterprise creates a .mvn/gradle-enterprise-custom-user-data dot groovv file'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ["micronaut-gradle-enterprise"])

        String customUser = output[".mvn/gradle-enterprise-custom-user-data.groovy"]

        then:
        customUser
        customUser.count("buildCache.remote.storeEnabled = System.getenv('GITHUB_ACTIONS') != null") == 1

    }

    @Unroll
    void 'feature micronaut-gradle-enterprise does not create maven files for Gradle'(BuildTool buildTool) {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), ["micronaut-gradle-enterprise"])

        then:
        !output[".mvn/gradle-enterprise.xml"]
        !output[".mvn/gradle-enterprise-custom-user-data.groovy"]
        !output[".mvn/extensions.xml"]

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void 'feature micronaut-gradle-enterprise creates a .mvn/gradle-enterprise dot xml file'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ["micronaut-gradle-enterprise"])

        String gradleEnterprise = output[".mvn/gradle-enterprise.xml"]

        then:
        gradleEnterprise.count('<gradleEnterprise>') == 1
        gradleEnterprise.count('<server>') == 2
        gradleEnterprise.count('<url>https://ge.micronaut.io</url>') == 1
        gradleEnterprise.count('</server>') == 2
        gradleEnterprise.count('<buildScan>') == 1
        gradleEnterprise.count('<publish>ALWAYS</publish>') == 1
        gradleEnterprise.count('</buildScan>') == 1
        gradleEnterprise.count('<buildCache>') == 1
        gradleEnterprise.count('<remote>') == 1
        gradleEnterprise.count('<credentials>') == 1
        gradleEnterprise.count('<username>${env.GRADLE_ENTERPRISE_CACHE_USERNAME}</username>') == 1
        gradleEnterprise.count('<password>${env.GRADLE_ENTERPRISE_CACHE_PASSWORD}</password>') == 1
        gradleEnterprise.count('</credentials>') == 1
        gradleEnterprise.count('</remote>') == 1
        gradleEnterprise.count('</buildCache>') == 1
        gradleEnterprise.count('</gradleEnterprise>') == 1
    }
}
