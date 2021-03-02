package io.micronaut.starter.feature.jib

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JibSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle jib feature for language=#language'() {
        when:
        String template = gradleTemplate(language, ['jib'])

        then:
        template.contains("image = \"gcr.io/myapp/jib-image\"")

        when:
        String pluginId = 'com.google.cloud.tools.jib'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        language << Language.values().toList()
    }

}
