package io.micronaut.starter.feature.grpc

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GrpcSpec extends ApplicationContextSpec {

    @Shared
    @Subject
    Grpc grpc = beanContext.getBean(Grpc)

    void "grpc belongs to API category"() {
        expect:
        Category.API == grpc.category
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
