package io.micronaut.starter.build.dependencies

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language


class FeatureWithDuplicatesSpec extends BeanContextSpec implements CommandOutputFixture {

    void "test dependencies added for feature-with-duplicates"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['feature-with-duplicates'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then: 'for a dependency in compile, runtime, compileOnly, testRuntimeOnly, testCompileOnly and test, only compile should be added'
        !verifier.hasDependency("org.gebish", "geb-core", Scope.RUNTIME)
        !verifier.hasDependency("org.gebish", "geb-core", Scope.TEST)
        !verifier.hasDependency("org.gebish", "geb-core", Scope.TEST_COMPILE_ONLY)
        !verifier.hasDependency("org.gebish", "geb-core", Scope.COMPILE_ONLY)
        !verifier.hasDependency("org.gebish", "geb-core", Scope.TEST_RUNTIME)
        verifier.hasDependency("org.gebish", "geb-core", Scope.COMPILE)

        and: 'for a dependency in runtime and test, both runtime and test should be added'
        verifier.hasDependency("org.seleniumhq.selenium", "selenium-firefox-driver", Scope.RUNTIME)
        verifier.hasDependency("org.seleniumhq.selenium", "selenium-firefox-driver", Scope.TEST)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
