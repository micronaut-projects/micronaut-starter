package io.micronaut.starter.feature.buildless

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.CoordinateResolver
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class BuildlessSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Override
    Map<String, Object> getConfiguration() {
        ["spec.name": "BuildlessSpec"]
    }

    void 'test README.md with buildless feature contains links to docs'() {
        when:
        def output = generate([Buildless.NAME])
        def readme = output["README.md"]

        then:
        readme.contains("[https://docs.less.build/](https://docs.less.build/)")
    }

    void "feature buildless properties validation"() {
        given:
        def feature = beanContext.getBean(Buildless)

        expect:
        for (applicationType in ApplicationType.values()) {
            assert feature.supports(applicationType)
        }

        feature.category == Category.DEV_TOOLS
        feature.name == Buildless.NAME
    }

    void "feature buildless is unsupported with maven and #language"(Language language) {
        when:
        generate(ApplicationType.DEFAULT, new Options(language, BuildTool.MAVEN), [Buildless.NAME])

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Feature only supported by Gradle'

        where:
        language << Language.values()
    }

    void "buildless configured correctly for #buildTool"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), [Buildless.NAME])
        def version = beanContext.getBean(CoordinateResolver).resolve(Buildless.BUILDLESS_PLUGIN_ARTIFACT).get().version
        def settings = buildTool == BuildTool.GRADLE ? output['settings.gradle'] : output['settings.gradle.kts']
        def expectedPlugin = buildTool == BuildTool.GRADLE ? """id "build.less" version "$version\"""" : """id("build.less") version("$version")"""

        then: 'we have the plugin in the settings file'
        settings.contains("    $expectedPlugin")

        and: 'we have the configuration in the settings file'
        settings.contains("""\
buildless {
    // this call is enough to configure the cache, although, remember to
    // set BUILDLESS_APIKEY in your environment.
}""")

        and: 'caching is enabled in the properties file'
        output['gradle.properties'].contains("org.gradle.caching=true")

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "io.micronaut.starter.feature.buildless.Buildless is visible"() {
        expect:
        beanContext.getBean(Buildless).visible
    }
}
