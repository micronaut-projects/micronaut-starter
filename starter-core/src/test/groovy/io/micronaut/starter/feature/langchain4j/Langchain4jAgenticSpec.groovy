package io.micronaut.starter.feature.langchain4j

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class Langchain4jAgenticSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Langchain4jAgentic feature = beanContext.getBean(Langchain4jAgentic)

    void "langchain4j-agentic belongs to AI category"() {
        expect:
        feature.category == Category.AI
    }

    void "langchain4j-agentic supports application type #applicationType"(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "langchain4j-agentic dependencies #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['langchain4j-agentic'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency('io.micronaut.langchain4j', 'micronaut-langchain4j-agentic', Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }

    void 'README.md with langchain4j-agentic contains documentation links'() {
        when:
        def output = generate(['langchain4j-agentic'])
        def readme = output['README.md']

        then:
        readme
        readme.contains('https://micronaut-projects.github.io/micronaut-mcp/latest/guide/#agenticService')
        readme.contains('https://docs.langchain4j.dev/tutorials/agents')
    }
}
