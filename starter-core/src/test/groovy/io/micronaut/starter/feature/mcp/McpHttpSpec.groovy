package io.micronaut.starter.feature.mcp

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class McpHttpSpec extends ApplicationContextSpec implements CommandOutputFixture{

    @Shared
    @Subject
    McpHttp mcpHttp = beanContext.getBean(McpHttp)

    void "mcp-http belongs to MCP category"() {
        expect:
        Category.MCP == mcpHttp.category
    }

    void "mcp-http supports application type #appType"(ApplicationType appType) {
        expect:
        mcpHttp.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "mcp-http dependencies  #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['mcp-http'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("micronaut-mcp-server-java-sdk")

        where:
        buildTool << BuildTool.values()
    }

    void "mcp-http renders MCP configuration"() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'mcp-http'])
        String config = output["src/main/resources/application.yml"]

        then:
        config
        config.contains("name: mcpdemo")
        config.contains("version: 0.0.1")
        config.contains("transport: HTTP")
    }
}
