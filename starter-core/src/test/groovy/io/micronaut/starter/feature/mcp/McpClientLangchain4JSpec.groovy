package io.micronaut.starter.feature.mcp

import io.micronaut.starter.ApplicationContextSpec
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

class McpClientLangchain4JSpec extends ApplicationContextSpec implements CommandOutputFixture{

    @Shared
    @Subject
    McpClientLangchain4j feature = beanContext.getBean(McpClientLangchain4j)

    void "mcp-client-langchain4j belongs to MCP category"() {
        expect:
        Category.MCP == feature.category
    }

    void "mcp-client-langchain4j supports application type #appType"(ApplicationType appType) {
        expect:
        feature.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "mcp-client-langchain4j dependencies  #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['mcp-client-langchain4j'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.mcp", "micronaut-mcp-client-langchain4j", Scope.TEST)

        where:
        buildTool << BuildTool.values().toList() - BuildTool.PYRONAUT
    }

    void 'test readme.md with feature mcp-client-langchain4j contains links to docs'() {
        when:
        def output = generate(['mcp-client-langchain4j'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mcp/latest/guide/#clientLangchain4j")
        readme.contains("https://docs.langchain4j.dev/tutorials/mcp/#mcp-client")
    }
}
