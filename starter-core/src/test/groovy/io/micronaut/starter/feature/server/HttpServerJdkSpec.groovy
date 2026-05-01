package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class HttpServerJdkSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature http-server-jdk contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['http-server-jdk'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-servlet/latest/guide/#httpServer")
    }

    void 'test #buildTool http-server-jdk feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["http-server-jdk"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template.contains("runtime(\"http_server_jdk\")") || template.contains("<micronaut.runtime>http_server_jdk</micronaut.runtime>")

        and: "Contains required dependencies"
        buildTool != BuildTool.MAVEN
                || verifier.hasDependency("io.micronaut.servlet", "micronaut-http-server-jdk")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }
    }
}
