package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class HttpPojaTestSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature http-poja contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['http-poja'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-servlet/latest/guide/index.html#httpPoja")
    }

    void 'test #buildTool http-poja feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["http-poja"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template.contains("runtime(\"http_poja\")") || template.contains("<micronaut.runtime>http_poja</micronaut.runtime>")

        and: "Contains required dependencies"
        buildTool != BuildTool.MAVEN
                || template.contains("micronaut-http-poja-apache") && template.contains("micronaut-http-poja-test")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
