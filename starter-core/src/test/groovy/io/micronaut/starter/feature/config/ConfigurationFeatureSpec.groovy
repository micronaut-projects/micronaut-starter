package io.micronaut.starter.feature.config

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class ConfigurationFeatureSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll
    void "properties is the default configuration feature no yaml dependencies are added #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("org.yaml", "snakeyaml", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }

    @Unroll
    void "properties is the default configuration feature no application.yml is generated and application.properties exists #buildTool"(BuildTool buildTool) {

        when:
        Map<String, String> output = generate()

        then:
        output['src/main/resources/application.properties']
        !output['src/main/resources/application.yml']

        where:
        buildTool << BuildTool.values()
    }
}
