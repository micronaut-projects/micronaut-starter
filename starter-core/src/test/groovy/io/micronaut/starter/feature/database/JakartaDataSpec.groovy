package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class JakartaDataSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test jakarta-data feature dependency for language=#language and buildTool=#buildTool'() {
        given:
        BuildTool buildTool = BuildTool.GRADLE
        Language language = Language.JAVA

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["jakarta-data"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("jakarta.data","jakarta.data-api")
    }

    void 'test readme has docs'() {
        when:
        Map<String, String> output = generate(["jakarta-data"])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://micronaut-projects.github.io/micronaut-data/latest/guide/#jakartaData')
        readme.contains('https://jakarta.ee/specifications/data/1.0/jakarta-data-1.0')
    }
}
