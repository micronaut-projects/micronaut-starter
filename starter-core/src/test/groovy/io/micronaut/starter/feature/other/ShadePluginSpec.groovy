package io.micronaut.starter.feature.other

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ShadePluginSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md by default contains a link to the Shadow Gradle Plugin in the plugin portal'() {
        when:
        String readme = generate([])["README.md"]

        then:
        readme
        readme.contains("[Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)")
    }

    @Unroll
    void 'test shade plugin is applied by default for Gradle and language=#language type=#applicationType'(Language language, ApplicationType applicationType) {
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
        
        where:
        [language, applicationType] << [Language.values(), [ApplicationType.FUNCTION, ApplicationType.DEFAULT]].combinations()
    }
}
