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

class MicronautMcpClientJavaSdkSpec extends ApplicationContextSpec implements CommandOutputFixture{

    @Shared
    @Subject
    MicronautMcpClientJavaSdk micronautMcpClientJavaSdk = beanContext.getBean(MicronautMcpClientJavaSdk)

    void "micronaut-mcp-client-java-sdk belongs to MCP category"() {
        expect:
        Category.MCP == micronautMcpClientJavaSdk.category
    }

    void "micronaut-mcp-client-java-sdk supports application type #appType"(ApplicationType appType) {
        expect:
        micronautMcpClientJavaSdk.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "micronaut-mcp-client-java-sdk dependencies  #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['micronaut-mcp-client-java-sdk'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("micronaut-mcp-client-java-sdk")

        where:
        buildTool << BuildTool.values()
    }

}
