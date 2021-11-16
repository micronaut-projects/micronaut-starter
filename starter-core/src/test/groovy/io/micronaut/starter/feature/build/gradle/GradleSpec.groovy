package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.Property
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Issue
import spock.lang.PendingFeature
import spock.lang.Unroll

class GradleSpec extends BeanContextSpec implements CommandOutputFixture {

    void "test settings.gradle"() {
        GradleBuild gradleBuild = new GradleBuild()
        String template = settingsGradle.template(buildProject(), gradleBuild).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
    }

    void "test gradle.properties"() {
        String template = gradleProperties.template([new Property() {
            String key = "name"
            String value = "Sally"
        }, new Property() {
            String key = "age"
            String value = "30"
        }]).render().toString()

        expect:
        template.contains('name=Sally')
        template.contains('age=30')
    }

    @Unroll
    @Issue('https://github.com/micronaut-projects/micronaut-starter/issues/601')
    void 'a Java/Groovy app with Gradle does not add a "tasks" block (language=#language)'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language, BuildTool.GRADLE))
        def buildGradle = output["build.gradle"]

        then:
        buildGradle
        !buildGradle.contains("tasks")

        where:
        language << [Language.JAVA, Language.GROOVY]
    }

    @Unroll
    @Issue('https://github.com/micronaut-projects/micronaut-starter/issues/601')
    void 'a Kotlin app with Gradle adds a "tasks" block (language=#language)'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.KOTLIN, BuildTool.GRADLE))
        def buildGradle = output["build.gradle"]

        then:
        buildGradle
        buildGradle.contains("tasks")
    }

    void 'disable Gradle Toolchain by default (language=#language)'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language, BuildTool.GRADLE))
        def gradleProperties = output["gradle.properties"]

        then:
        gradleProperties
        gradleProperties.contains("org.gradle.java.installations.auto-download=false")
        gradleProperties.contains("org.gradle.java.installations.auto-detect=false")
        gradleProperties.contains("org.gradle.java.installations.fromEnv=JAVA_HOME")

        where:
        language << Language.values()
    }

}
