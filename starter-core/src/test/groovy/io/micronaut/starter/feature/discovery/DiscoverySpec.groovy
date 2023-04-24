package io.micronaut.starter.feature.discovery

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class DiscoverySpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test there can only be one discovery feature'() {
        when:
        getFeatures(["discovery-consul", "discovery-eureka"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

    void 'test readme.md with feature discovery-core contains links to micronaut docs'() {
        when:
        def output = generate(['discovery-core'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-discovery-client/latest/guide/")
    }

    @Unroll
    void 'test gradle discovery-core feature for language=#language and buildTool=#buildTool'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['discovery-consul'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-discovery-core", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }


}
