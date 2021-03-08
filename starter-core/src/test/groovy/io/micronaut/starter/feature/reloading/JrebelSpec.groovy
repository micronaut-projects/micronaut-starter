package io.micronaut.starter.feature.reloading

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JrebelSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jrebel contains links to micronaut docs'() {
        when:
        def output = generate(['jrebel'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#jrebel")
    }

    @Unroll
    void 'test jrebel with Gradle for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['jrebel'])
                .render()

        String applyPlugin = 'id("org.zeroturnaround.gradle.jrebel") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion('org.zeroturnaround.gradle.jrebel', template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        language << Language.values()
    }
}
