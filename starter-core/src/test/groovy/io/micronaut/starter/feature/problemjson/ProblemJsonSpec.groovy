package io.micronaut.starter.feature.problemjson

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ProblemJsonSpec extends ApplicationContextSpec implements CommandOutputFixture {
    void 'test readme.md with feature problem-json contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['problem-json'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/index.html")
    }

    @Unroll
    void 'test maven problem-json feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['problem-json'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.problem", "micronaut-problem-json", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    @Unroll
    void 'test gradle problem-json feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['problem-json'])
                .render()

        then:
        template.contains('implementation("io.micronaut.problem:micronaut-problem-json")')

        where:
        language << Language.values().toList()
    }
}
