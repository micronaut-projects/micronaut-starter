package io.micronaut.starter.build

import io.micronaut.context.annotation.Requires
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import jakarta.inject.Singleton

class RequiresMavenLocalSpec extends BeanContextSpec implements CommandOutputFixture {
    @Override
    Map<String, String> getProperties() {
        Collections.singletonMap("spec.name", "RequiresMavenLocalSpec")
    }

    void "build contains mavenLocal() repository if the feature implements RequiresMavenLocal"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["maven-local-feature"])
                .render()

        then:
        template.contains("mavenLocal()")

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    @Requires(property = "spec.name", value = "RequiresMavenLocalSpec")
    @Singleton
    static class MavenLocalFeature implements RequiresMavenLocal {

        @Override
        String getName() {
            "maven-local-feature"
        }

        @Override
        boolean supports(ApplicationType applicationType) {
            return true;
        }
    }
}
