package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.Property
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.feature.build.MicronautBuildPlugin
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Unroll

class GradleSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    Gradle gradle = beanContext.getBean(Gradle)

    void "gradle is a BuildFeature"() {
        expect:
        gradle.isGradle()
        !gradle.isMaven()
    }

    void "test settings.gradle"() {
        GradleBuild gradleBuild = new GradleBuild()
        String template = settingsGradle.template(buildProject(), gradleBuild, false, ['app']).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
        !template.contains('include("app")')
    }

    void "test settings.gradle with multi-project feature and one module"() {
        GradleBuild gradleBuild = new GradleBuild()
        String template = settingsGradle.template(buildProject(), gradleBuild, true, ['app']).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
        template.contains('include("app")')
    }

    void "test settings.gradle without multi-project feature and two modules"() {
        GradleBuild gradleBuild = new GradleBuild()
        String template = settingsGradle.template(buildProject(), gradleBuild, false, ['app1', 'app2']).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
        template.contains('include("app1")')
        template.contains('include("app2")')
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

    void 'disable Gradle Toolchain by default (dsl = #dsl)'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, dsl))
        def buildGradle = output[fileName]

        then:
        buildGradle
        buildGradle.contains(configuration)

        where:
        dsl                     | fileName           | configuration
        BuildTool.GRADLE        | 'build.gradle'     | 'graalvmNative.toolchainDetection = false'
        BuildTool.GRADLE_KOTLIN | 'build.gradle.kts' | 'graalvmNative.toolchainDetection.set(false)'

    }

    void 'disable Gradle Toolchain by default for Oracle function (dsl = #dsl)'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, dsl), ['oracle-function'])
        def buildGradle = output[fileName]

        then:
        buildGradle
        buildGradle.contains(configuration)

        where:
        dsl                     | fileName           | configuration
        BuildTool.GRADLE        | 'build.gradle'     | 'graalvmNative.toolchainDetection = false'
        BuildTool.GRADLE_KOTLIN | 'build.gradle.kts' | 'graalvmNative.toolchainDetection.set(false)'
    }

    void 'Readme has Gradle and Graalvm plugin docs (lang = #lang, buildTool = #buildTool, apptype = #apptype)'(
            ApplicationType apptype, Language lang, BuildTool buildTool
    ) {
        when:
        def output = generate(apptype, new Options(lang, buildTool))
        def readme = output["README.md"]

        then:
        readme
        readme.contains(MicronautBuildPlugin.MICRONAUT_GRADLE_DOCS_URL)
        readme.contains(MicronautBuildPlugin.GRAALVM_GRADLE_DOCS_URL)

        where:
        [lang, buildTool, apptype] << [
                Language.values().toList() - Language.GROOVY,
                BuildTool.valuesGradle(),
                ApplicationType.values().toList() - ApplicationType.FUNCTION
        ].combinations()
    }

    void 'Readme has Gradle but no GraalVM plugin docs (lang = #lang, buildTool = #buildTool, apptype = #apptype)'(
            ApplicationType apptype, Language lang, BuildTool buildTool
    ) {
        when:
        def output = generate(apptype, new Options(lang, buildTool))
        def readme = output["README.md"]

        then:
        readme
        readme.contains(MicronautBuildPlugin.MICRONAUT_GRADLE_DOCS_URL)
        !readme.contains(MicronautBuildPlugin.GRAALVM_GRADLE_DOCS_URL)

        where:
        [lang, buildTool, apptype] << [
                Language.values().toList(),
                BuildTool.valuesGradle(),
                [ApplicationType.FUNCTION]
        ].combinations()
    }
}
