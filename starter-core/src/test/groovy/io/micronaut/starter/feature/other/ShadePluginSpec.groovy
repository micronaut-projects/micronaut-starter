package io.micronaut.starter.feature.other

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ShadePluginSpec extends ApplicationContextSpec {

    @Unroll
    void 'test shade plugin is applied by default for Gradle and language=#language'(Language language, ApplicationType applicationType) {
        given:
        String pluginId = 'com.github.johnrengelman.shadow'
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .applicationType(applicationType)
                .render()

        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        and:
        String dependency = 'assemble.dependsOn("shadowJar")'
        if (applicationType == ApplicationType.DEFAULT) {
            assert !template.contains(dependency)
        } else if (applicationType == ApplicationType.FUNCTION) {
            assert template.contains(dependency)
        }
        where:
        [language, applicationType] << [Language.values(), [ApplicationType.FUNCTION, ApplicationType.DEFAULT]].combinations()
    }
}
