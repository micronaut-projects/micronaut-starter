package io.micronaut.starter.feature.build

import groovy.namespace.QName
import groovy.xml.XmlParser
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.build.gradle.GradlePluginPortal
import io.micronaut.starter.build.gradle.GradleRepository
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import jakarta.inject.Singleton
import spock.lang.Subject

class MicronautGradleEnterpriseSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    MicronautGradleEnterprise micronautGradleEnterprise = beanContext.getBean(MicronautGradleEnterprise.class)

    @Override
    Map<String, Object> getConfiguration() {
        ["spec.name": "MicronautGradleEnterpriseSpec"]
    }

    void "if you add micronaut-gradle-enterprise it is configured for #buildTool"() {
        given:when:
        BuildBuilder builder = new BuildBuilder(beanContext, buildTool)
                    .language(Language.JAVA)
                    .applicationType(ApplicationType.DEFAULT)
                    .features(["micronaut-gradle-enterprise"])
        Project project = builder.getProject()
        GradleBuild gradleBuild = (GradleBuild) builder.build(false)
        String settings = settingsGradle.template(project, gradleBuild, false, []).render().toString()

        then: 'we have 3 plugin repositories as in the replacement below'
        micronautGradleEnterprise.pluginsManagementRepositories().size() == 3

        and: 'we have a single plugin management block'
        settings.count('pluginManagement {') == 1

        and: 'duplicate repositories are not added'
        settings.contains('''pluginManagement {
          |    repositories {
          |        gradlePluginPortal()
          |        mavenCentral()
          |    }
          |}'''.stripMargin())

        settings.contains('plugins {')
        if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert settings.contains('    id("io.micronaut.build.internal.gradle-enterprise") version("')
        } else if (buildTool == BuildTool.GRADLE) {
            assert settings.contains('    id "io.micronaut.build.internal.gradle-enterprise" version "')
        }

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "io.micronaut.starter.feature.build.MicronautGradleEnterprise is not visible"() {
        expect:
        !beanContext.getBean(MicronautGradleEnterprise).isVisible()
    }

    void 'feature micronaut-gradle-enterprise creates a .mvn/extensions dot xml file'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ["micronaut-gradle-enterprise"])
        def xml = new XmlParser().parseText(output[".mvn/extensions.xml"])

        then:
        xml.name() == new QName('https://maven.apache.org/EXTENSIONS/1.0.0', 'extensions')

        def enterpriseExtension = xml.extension.find { it.artifactId.text() == 'gradle-enterprise-maven-extension' }
        enterpriseExtension.groupId.text() == 'com.gradle'
        enterpriseExtension.version.text() ==~ /[\d.]+/ // numbers and fullstops
        def userDataExtension = xml.extension.find { it.artifactId.text() == 'common-custom-user-data-maven-extension' }
        userDataExtension.groupId.text() == 'com.gradle'
        userDataExtension.version.text() ==~ /[\d.]+/ // numbers and fullstops
    }

    void 'feature micronaut-gradle-enterprise creates a .mvn/gradle-enterprise-custom-user-data dot groovy file'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ["micronaut-gradle-enterprise"])

        then:
        output[".mvn/gradle-enterprise-custom-user-data.groovy"] == "buildCache.remote.storeEnabled = System.getenv('GITHUB_ACTIONS') != null"
    }

    void 'feature micronaut-gradle-enterprise does not create maven files for #buildTool'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), ["micronaut-gradle-enterprise"])

        then:
        output[".mvn/gradle-enterprise.xml"] == null
        output[".mvn/gradle-enterprise-custom-user-data.groovy"] == null
        output[".mvn/extensions.xml"] == null

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void 'feature micronaut-gradle-enterprise creates a .mvn/gradle-enterprise dot xml file'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), ["micronaut-gradle-enterprise"])
        def xml = new XmlParser().parseText(output[".mvn/gradle-enterprise.xml"])

        then:
        xml.name() == new QName('https://www.gradle.com/gradle-enterprise-maven', 'gradleEnterprise')
        xml.server.url.text() == 'https://ge.micronaut.io'
        xml.buildScan.publish.text() == 'ALWAYS'
        !xml.buildCache.remote.storeEnabled // Not set in here, this is handled in custom data
    }

    @Singleton
    @Replaces(MicronautGradleEnterprise.class)
    @Requires(property = "spec.name", value = "MicronautGradleEnterpriseSpec")
    static class MicronautGradleEnterpriseReplacement extends MicronautGradleEnterprise {
        @Override
        protected List<GradleRepository> pluginsManagementRepositories() {
            super.pluginsManagementRepositories() + new GradlePluginPortal()
        }
    }
}
