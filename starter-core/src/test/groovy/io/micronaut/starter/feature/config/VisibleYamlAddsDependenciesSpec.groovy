package io.micronaut.starter.feature.config

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Options
import jakarta.inject.Singleton
import spock.lang.Shared
import spock.lang.Subject

class VisibleYamlAddsDependenciesSpec extends BeanContextSpec implements CommandOutputFixture {

    @Override
    Map<String, String> getProperties() {
        Collections.singletonMap("spec.name", "VisibleYamlAddsDependenciesSpec")
    }

    @Shared
    @Subject
    VisibleYaml yaml = beanContext.getBean(VisibleYaml)

    @Requires(property = "spec.name", value = "VisibleYamlAddsDependenciesSpec")
    @Singleton
    @Replaces(Yaml.class)
    static class VisibleYaml extends Yaml {
        @Override
        boolean isVisible() {
            return true
        }
    }

    void "test dependency added for yaml feature"() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options())

        then:
        output["build.gradle"].contains('runtimeOnly("org.yaml:snakeyaml"')
        output["build.gradle"].contains('testRuntimeOnly("org.yaml:snakeyaml"')
    }
}
