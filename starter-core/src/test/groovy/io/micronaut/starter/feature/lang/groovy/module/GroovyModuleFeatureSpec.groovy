package io.micronaut.starter.feature.lang.groovy.module

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class GroovyModuleFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test feature #moduleFeature with build tool #buildTool and Groovy language"(String moduleFeature, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(Language.GROOVY)
                .features([moduleFeature])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.apache.groovy", moduleFeature, Scope.TEST)

        where:
        [moduleFeature, buildTool] << [beanContext.getBeansOfType(GroovyModuleFeature)*.name, BuildTool.values()].combinations()
    }

    void "test feature #moduleFeature with build tool #buildTool and Spock Framework"(String moduleFeature, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(Language.JAVA)
                .testFramework(TestFramework.SPOCK)
                .features([moduleFeature])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.apache.groovy", moduleFeature, Scope.TEST)

        where:
        [moduleFeature, buildTool] << [beanContext.getBeansOfType(GroovyModuleFeature)*.name, BuildTool.values()].combinations()
    }

    void "test groovy feature #moduleFeature with language #language and test framework #testFramework fails"(
            String moduleFeature, BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        new BuildBuilder(beanContext, buildTool)
                .language(language)
                .testFramework(testFramework)
                .features([moduleFeature])
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "$moduleFeature requires Groovy language or Spock test framework."

        where:
        [moduleFeature, buildTool, language, testFramework] << [
                beanContext.getBeansOfType(GroovyModuleFeature)*.name,
                BuildTool.values(),
                Language.values() - Language.GROOVY,
                TestFramework.values() - TestFramework.SPOCK
        ].combinations()
    }

    void "test groovy feature #moduleFeature.name properties"(GroovyModuleFeature moduleFeature) {
        expect:
        def name = moduleFeature.name
        moduleFeature.category == Category.GROOVY_MODULE
        moduleFeature.description == "Groovy optional module ${name}"
        moduleFeature.title == "${name} module"

        where:
        moduleFeature << beanContext.getBeansOfType(GroovyModuleFeature)
    }
}
