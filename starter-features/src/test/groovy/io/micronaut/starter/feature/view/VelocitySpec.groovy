package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VelocitySpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-velocity contains links to micronaut docs'() {
        when:
        def output = generate(['views-velocity'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://velocity.apache.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#velocity")
    }

    @Unroll
    void 'test #buildTool views-velocity feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['views-velocity'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-velocity", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
