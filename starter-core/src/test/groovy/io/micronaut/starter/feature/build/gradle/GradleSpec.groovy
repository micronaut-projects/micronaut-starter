package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.Property
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.build.Kapt
import io.micronaut.starter.feature.build.MicronautBuildPlugin
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Issue
import spock.lang.Shared

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

    @Issue('https://github.com/micronaut-projects/micronaut-starter/issues/601')
    void 'a Java/Groovy app with Gradle does not add a "tasks" block (language=#language)'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language, BuildTool.GRADLE))
        def buildGradle = output["build.gradle"]

        then:
        buildGradle
        !buildGradle.contains("tasks {")

        where:
        language << [Language.JAVA, Language.GROOVY]
    }

    void 'disable Gradle Toolchain by default (dsl = #dsl)'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, dsl))
        String buildGradle = output[fileName]

        then:
        buildGradle
        buildGradle.contains(configuration)

        where:
        dsl                     | fileName           | configuration
        BuildTool.GRADLE        | 'build.gradle'     | 'graalvmNative.toolchainDetection = false'
        BuildTool.GRADLE_KOTLIN | 'build.gradle.kts' | 'graalvmNative.toolchainDetection.set(false)'
    }

    void 'ignoredAutomaticDependencies not output by default'() {
        when:
        Map<String, String> output = generate([])

        then:
        !output["build.gradle.kts"].contains('ignoredAutomaticDependencies')
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

    void 'Supported languages have both Gradle and Graalvm plugin docs (lang = #lang, buildTool = #buildTool, apptype = #apptype)'(
            ApplicationType apptype, Language lang, BuildTool buildTool
    ) {
        when:
        List<String> features = (apptype == ApplicationType.CLI && lang == Language.KOTLIN) ? [Kapt.NAME] : []
        Map<String, String> output = generate(apptype, new Options(lang, TestFramework.DEFAULT_OPTION, buildTool, jdk), features)
        String readme = output["README.md"]

        then:
        readme
        readme.contains(MicronautBuildPlugin.MICRONAUT_GRADLE_DOCS_URL)
        readme.contains(MicronautBuildPlugin.GRAALVM_GRADLE_DOCS_URL)

        where:
        [lang, jdk, buildTool, apptype] << [
                GraalVMFeatureValidator.supportedLanguages(),
                AwsLambdaFeatureValidator.supportedJdks(),
                BuildTool.valuesGradle(),
                ApplicationType.values().toList()
        ].combinations()
    }

    void 'Selected jdk = #jdk is specified in build = #buildTool for lang = #lang'(
            Language lang, JdkVersion jdk, BuildTool buildTool
    ) {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(lang, TestFramework.DEFAULT_OPTION, buildTool, jdk))
        def buildFile = buildTool == BuildTool.GRADLE ? output["build.gradle"] : output["build.gradle.kts"]

        then:
        buildFile
        buildFile.contains("sourceCompatibility = JavaVersion.toVersion(\"${jdk.majorVersion()}\")")
        if (lang == Language.KOTLIN) {
            assert !buildFile.contains("targetCompatibility = JavaVersion.toVersion(\"${jdk.majorVersion()}\")")
        } else {
            assert buildFile.contains("targetCompatibility = JavaVersion.toVersion(\"${jdk.majorVersion()}\")")
        }

        where:
        [lang, jdk, buildTool] << [
                Language.values(),
                [JdkVersion.JDK_17, JdkVersion.JDK_21],
                BuildTool.valuesGradle()
        ].combinations()
    }

    void 'Unsupported languages have Gradle but omit GraalVM plugin docs (lang = #lang, buildTool = #buildTool, apptype = #apptype)'(
            ApplicationType apptype, Language lang, BuildTool buildTool
    ) {
        when:
        Map<String, String> output = generate(apptype, new Options(lang, TestFramework.DEFAULT_OPTION, buildTool, jdk))
        String readme = output["README.md"]

        then:
        readme
        readme.contains(MicronautBuildPlugin.MICRONAUT_GRADLE_DOCS_URL)
        !readme.contains(MicronautBuildPlugin.GRAALVM_GRADLE_DOCS_URL)

        where:
        [lang, jdk, buildTool, apptype] << [
                Language.values().toList() - GraalVMFeatureValidator.supportedLanguages(),
                AwsLambdaFeatureValidator.supportedJdks(),
                BuildTool.valuesGradle(),
                ApplicationType.values().toList()
        ].combinations()
    }
}
