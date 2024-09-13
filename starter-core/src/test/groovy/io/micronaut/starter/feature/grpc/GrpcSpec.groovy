package io.micronaut.starter.feature.grpc

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GrpcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with grpc application type contains a link to the Grpc Gradle Plugin in the plugin portal'() {
        when:
        String readme = generate(ApplicationType.GRPC, [])["README.md"]

        then:
        readme
        readme.contains("[Protobuf Gradle Plugin](https://plugins.gradle.org/plugin/com.google.protobuf)")
    }

    @Shared
    @Subject
    Grpc grpc = beanContext.getBean(Grpc)

    void "grpc belongs to API category"() {
        expect:
        Category.API == grpc.category
    }


    void 'test grpc plugin is applied by default for Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .applicationType(ApplicationType.GRPC)
                .language(Language.JAVA)
                .render()

        then:
        template.contains("<groupId>com.github.os72</groupId>")
        template.contains("<artifactId>protoc-jar-maven-plugin</artifactId>")
    }

    @Unroll
    void 'test grpc plugin is applied by default for Gradle and language=#language'(Language language) {
        given:
        String pluginId = 'com.google.protobuf'

        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .applicationType(ApplicationType.GRPC)
                .language(language)
                .render()

        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        language << Language.values()
    }
}
