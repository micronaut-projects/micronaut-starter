package io.micronaut.starter.feature.micrometer

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import spock.lang.Unroll

class PrometheusPushGatewaySpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll("feature #featureName adds compile dependency #dependency")
    void "micrometer-prometheus-pushgateway adds dependencies"(String featureName, String groupId, String artifactId, String dependency) {
        given:
        Language language = Language.JAVA
        BuildTool buildTool = BuildTool.GRADLE

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([featureName])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency(groupId, artifactId, Scope.COMPILE)

        where:
        featureName                                                | groupId                                 | artifactId
        "micrometer-prometheus-pushgateway"  |  "io.micronaut.micrometer" | "micronaut-micrometer-registry-prometheus-pushgateway"
        dependency = "${groupId}:${artifactId}"
    }
}
