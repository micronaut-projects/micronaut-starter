package io.micronaut.starter.feature.mcp

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class MicronautMcpClientLangchain4jSpec extends ApplicationContextSpec implements CommandOutputFixture{

    @Shared
    @Subject
    MicronautMcpClientLangchain4j micronautMcpClientLangchain4j = beanContext.getBean(MicronautMcpClientLangchain4j)

    void "micronaut-mcp-client-langchain4j belongs to MCP category"() {
        expect:
        Category.MCP == micronautMcpClientLangchain4j.category
    }

    void "micronaut-mcp-client-langchain4j supports application type #appType"(ApplicationType appType) {
        expect:
        micronautMcpClientLangchain4j.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "micronaut-mcp-client-langchain4j dependencies  #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['micronaut-mcp-client-langchain4j'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("micronaut-mcp-client-langchain4j")

        where:
        buildTool << BuildTool.values()
    }

}
