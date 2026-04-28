package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.aws.DynamoDb
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class JettyTestSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jetty-server contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['jetty-server'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-servlet/latest/guide/index.html#jetty")
    }

    void 'test #buildTool jetty-server feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["jetty-server"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template.contains("runtime(\"jetty\")") || template.contains(">jetty</")

        and: 'processor is applied'
        verifier.hasAnnotationProcessor("io.micronaut.servlet", "micronaut-servlet-processor")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
