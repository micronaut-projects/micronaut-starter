package io.micronaut.starter.feature.other

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ShadePluginSpec extends ApplicationContextSpec {

    @Unroll
    void 'test shade plugin is applied by default for Gradle and language=#language'(Language language) {
        given:
        String pluginId = 'com.github.johnrengelman.shadow'
        when:
        String template = gradleTemplate(language, [])

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
