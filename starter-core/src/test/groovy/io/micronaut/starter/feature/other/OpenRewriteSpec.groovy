package io.micronaut.starter.feature.other

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN
import static io.micronaut.starter.options.Language.JAVA

class OpenRewriteSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature openrewrite contains a link to the Rewrite Gradle Plugin in the plugin portal'() {
        when:
        String readme = generate(['openrewrite'])["README.md"]

        then:
        readme
        readme.contains("[Rewrite Gradle Plugin](https://plugins.gradle.org/plugin/org.openrewrite.rewrite)")
    }

    void 'test Gradle openrewrite feature'() {
        when:
        String template = render(GRADLE)
        String pluginId = 'org.openrewrite.rewrite'

        then:
        template.contains 'rewrite("org.openrewrite.recipe:rewrite-micronaut:'

        template.contains '''
rewrite {
    activeRecipe("org.openrewrite.java.micronaut.Micronaut2to3Migration")
}
'''

        template.contains 'id("' + pluginId + '") version "'

        when:
        Optional<SemanticVersion> semver = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semver.isPresent()
    }

    void 'test Maven openrewrite feature'() {
        when:
        String template = render(MAVEN)

        then:

        parsePropertySemanticVersion(template, 'openrewrite.maven.plugin.version').isPresent()
        parsePropertySemanticVersion(template, 'rewrite-micronaut.version').isPresent()

        template.contains '''
      <plugin>
        <groupId>org.openrewrite.maven</groupId>
        <artifactId>rewrite-maven-plugin</artifactId>
        <version>${openrewrite.maven.plugin.version}</version>
        <configuration>
          <activeRecipes>
            <recipe>org.openrewrite.java.micronaut.Micronaut2to3Migration</recipe>
          </activeRecipes>
          <activeStyles>
          </activeStyles>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-micronaut</artifactId>
            <version>${rewrite-micronaut.version}</version>
          </dependency>
        </dependencies>
      </plugin>
'''
    }

    private String render(BuildTool buildTool) {
        new BuildBuilder(beanContext, buildTool)
                .language(JAVA)
                .features(['openrewrite'])
                .render()
    }
}
