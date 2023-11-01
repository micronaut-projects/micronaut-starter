package io.micronaut.starter.feature.discovery

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.httpclient.HttpClientJdk
import io.micronaut.starter.options.BuildTool

class DiscoveryClientSpec extends BeanContextSpec {
    void "dependencies for discovery client feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([DiscoveryClient.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.discovery", "micronaut-discovery-client", Scope.COMPILE)
        verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }

    void "dependencies for discovery client and http-client-jdk"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([DiscoveryClient.NAME, HttpClientJdk.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.discovery", "micronaut-discovery-client", Scope.COMPILE)
        verifier.hasDependency("io.micronaut", HttpClientJdk.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK, Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }
}
