package io.micronaut.starter.feature

import groovy.xml.XmlParser
import io.micronaut.context.annotation.Requires
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.RequiresMavenLocal
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import jakarta.inject.Singleton

class RequiresMavenLocalSpec extends BeanContextSpec implements CommandOutputFixture {
    @Override
    Map<String, String> getProperties() {
        Collections.singletonMap("spec.name", "RequiresMavenLocalSpec")
    }

    void "Gradle build contains mavenLocal() repository if the feature implements RequiresMavenLocal"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["maven-local-feature"])
                .render()

        then:
        template.contains("mavenLocal()")

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void "Maven build contains mavenLocal() repository if the feature implements RequiresMavenLocal"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["maven-local-feature"])
                .render()

        def pom = new XmlParser().parseText(template)

        then:
        with(pom.repositories.repository.find { it.id.text() == "local" }) {
            url.text() == '${user.home}/.m2/repository/'
        }
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
