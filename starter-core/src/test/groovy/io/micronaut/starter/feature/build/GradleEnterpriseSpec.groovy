package io.micronaut.starter.feature.build

import groovy.namespace.QName
import groovy.xml.XmlParser
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.Project
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver
import io.micronaut.projectgen.core.buildtools.gradle.GradleBuild
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
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
        settings.contains($/id("com.gradle.enterprise") version("$expectedVersion")/$)

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void 'feature gradle-enterprise creates a .mvn/extensions dot xml file'() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).language(Language.JAVA).buildTool(BuildTool.MAVEN).features(["gradle-enterprise"]).build())
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

    void 'feature gradle-enterprise does not create maven files for #buildTool'() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).language(Language.JAVA).buildTool(buildTool).features(["gradle-enterprise"]).build())

        then:
        output[".mvn/gradle-enterprise.xml"] == null
        output[".mvn/extensions.xml"] == null

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void 'feature gradle-enterprise creates a .mvn/gradle-enterprise dot xml file'() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).language(Language.JAVA).buildTool(BuildTool.MAVEN).features(["gradle-enterprise"]).build())
        def xml = new XmlParser().parseText(output[".mvn/gradle-enterprise.xml"])

        then:
        xml.name() == new QName('https://www.gradle.com/gradle-enterprise-maven', 'gradleEnterprise')
        !xml.server
        xml.buildScan.termsOfService.url.text() == 'https://gradle.com/terms-of-service'
        xml.buildScan.termsOfService.accept.text() == 'true'
        xml.buildScan.publish.text() == 'ALWAYS'
    }
}
