package io.micronaut.starter.feature.asciidoctor

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AsciidoctorSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle asciidoctor feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['asciidoctor'])
                .render()

        then:
        template.contains("apply from: \"gradle/asciidoc.gradle\"")

        when:
        String pluginId = 'org.asciidoctor.jvm.convert'
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

    @Unroll
    void 'test maven asciidoctor feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['asciidoctor'])
                .render()

        then:
        template.contains('''\
        <groupId>org.asciidoctor</groupId>
        <artifactId>asciidoctor-maven-plugin</artifactId>
        <version>${asciidoctor.maven.plugin.version}</version>
        <dependencies>
          <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj</artifactId>
            <version>${asciidoctorj.version}</version>
          </dependency>
          <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj-diagram</artifactId>
            <version>${asciidoctorj.diagram.version}</version>
          </dependency>
        </dependencies>
''')

        and:
        parsePropertySemanticVersion(template, "asciidoctor.maven.plugin.version").isPresent()

        where:
        language << Language.values().toList()
    }

}
