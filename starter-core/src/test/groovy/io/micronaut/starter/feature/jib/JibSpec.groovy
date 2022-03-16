package io.micronaut.starter.feature.jib

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JibSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature jib contains a link to the Jib Gradle Plugin in the plugin portal'() {
        when:
        String readme = generate(['jib'])["README.md"]

        then:
        readme
        readme.contains("[Jib Gradle Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)")
    }

    @Unroll
    void 'test gradle jib feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jib'])
                .language(language)
                .render()

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
