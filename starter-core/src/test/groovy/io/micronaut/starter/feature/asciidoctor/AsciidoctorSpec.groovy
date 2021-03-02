package io.micronaut.starter.feature.asciidoctor

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AsciidoctorSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle asciidoctor feature for language=#language'() {
        when:
        String template = gradleTemplate(language, ['asciidoctor'])

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
        String template = mavenTemplate(language, ['asciidoctor'])

        then:
        template.contains("""
        <groupId>org.asciidoctor</groupId>
        <artifactId>asciidoctor-maven-plugin</artifactId>
        <version>\${asciidoctor.maven.plugin.version}</version>
        <dependencies>
          <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj</artifactId>
            <version>\${asciidoctorj.version}</version>
          </dependency>
          <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj-diagram</artifactId>
            <version>\${asciidoctorj.diagram.version}</version>
          </dependency>
        </dependencies>
""")

        where:
        language << Language.values().toList()
    }

}
