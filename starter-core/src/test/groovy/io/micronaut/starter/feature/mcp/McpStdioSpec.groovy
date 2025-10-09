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

class McpStdioSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    McpStdio mcpStdio = beanContext.getBean(McpStdio)

    void "mcp-stdio belongs to MCP category"() {
        expect:
        Category.MCP == mcpStdio.category
    }

    void "mcp-stdio supports application type #appType"(ApplicationType appType) {
        expect:
        mcpStdio.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "mcp-stdio dependencies and excludes netty for buildTool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['mcp-stdio'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("micronaut-mcp-server-java-sdk")
        !verifier.hasDependency("io.micronaut", "micronaut-http-server-netty")
        (buildTool.isGradle() ? !template.contains('runtime("netty")') : true)

        where:
        buildTool << BuildTool.values()
    }

    void "mcp-stdio renders MCP configuration"() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'mcp-stdio'])
        String config = output["src/main/resources/application.yml"]

        then:
        config
        config.contains("name: mcpdemo")
        config.contains("version: 0.0.1")
        config.contains("transport: STDIO")
    }

    void 'test readme.md with feature mcp-stdio contains links to docs'() {
        when:
        def output = generate(['mcp-stdio'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mcp/latest/guide/#stdio")
        readme.contains("https://modelcontextprotocol.io/docs/getting-started/intro")
    }

    void "mcp-stdio configures logback.xml to STDERR"() {
        when:
        Map<String, String> output = generate(['mcp-stdio'])
        String logback = output["src/main/resources/logback.xml"]

        then:
        logback
        logback.contains('<appender name="STDERR"')
        logback.contains('<target>System.err</target>')
    }

    void "mcp-stdio disables Micronaut banner in Application main"() {
        when:
        Map<String, String> output = generate(['mcp-stdio'])
        String appJava = output.find { it.key.endsWith('/Application.java') }?.value
        String appGroovy = output.find { it.key.endsWith('/Application.groovy') }?.value
        String appKotlin = output.find { it.key.endsWith('/Application.kt') }?.value

        then:
        (appJava ?: appGroovy ?: appKotlin)
        (appJava?.contains('.banner(false)') ?: false) ||
        (appGroovy?.contains('.banner(false)') ?: false) ||
        (appKotlin?.contains('.banner(false)') ?: false)
    }
}
