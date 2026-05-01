package io.micronaut.starter.feature.dependencies

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.micrometer.MicrometerFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DependenciesFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': 'DependenciesFeatureSpec']
    }

    @Unroll
    void 'test gradle geb feature for language=#language and spock'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['geb'])
                .language(language)
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains('testImplementation("org.gebish:geb-spock:4.0")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-firefox-driver:3.141.59")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-support:3.141.59")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle geb feature for language=#language and junit'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['geb'])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.gebish:geb-junit5:')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-firefox-driver:')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-support:')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle mybatis feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['mybatis'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.mybatis", "mybatis", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }
    }

    @Unroll
    void 'test maven geb feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['geb'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        if (language == Language.GROOVY) {
            assert verifier.hasDependency("org.gebish", "geb-spock", Scope.TEST)
        } else {
            assert verifier.hasDependency("org.gebish", "geb-junit5", Scope.TEST)
        }


        and:
        verifier.hasDependency("org.seleniumhq.selenium", "selenium-firefox-driver", Scope.TEST_RUNTIME)
        verifier.hasDependency("org.seleniumhq.selenium", "selenium-support", Scope.TEST_RUNTIME)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }
    }
}
