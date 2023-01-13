package io.micronaut.starter.feature.config

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import jakarta.inject.Singleton
import spock.lang.Unroll

class VisibleYamlAddsDependenciesSpec extends BeanContextSpec implements CommandOutputFixture {

    @Override
    Map<String, String> getProperties() {
        Collections.singletonMap("spec.name", "VisibleYamlAddsDependenciesSpec")
    }

    @Requires(property = "spec.name", value = "VisibleYamlAddsDependenciesSpec")
    @Singleton
    @Replaces(Yaml.class)
    static class VisibleYaml extends Yaml {
        @Override
        boolean isVisible() {
            return true
        }
    }

    @Unroll
    void "test dependency added for yaml feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Yaml.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.yaml", "snakeyaml", Scope.RUNTIME)
        verifier.hasDependency("org.yaml", "snakeyaml", Scope.TEST_RUNTIME)

        where:
        buildTool << BuildTool.values()
    }
}
