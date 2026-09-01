package io.micronaut.starter.feature.view

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JTESpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-jte contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['views-jte'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://jte.gg/")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/#jte")
    }

    @Unroll
    void 'test gradle views-jte feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['views-jte'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-jte")')
        template.contains('''\
jte {
    sourceDirectory = file("src/main/jte").toPath()
    generate()
}''')

        when:
        String pluginId = 'gg.jte.gradle'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        language << supportedLanguages(BuildTool.GRADLE)
    }

    @Unroll
    void 'test maven views-jte feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-jte'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-jte", Scope.COMPILE)
        template.contains('''\
      <plugin>
        <groupId>gg.jte</groupId>
        <artifactId>jte-maven-plugin</artifactId>
        <version>''')
        template.contains('''</version>
          <configuration>
            <sourceDirectory>src/main/jte</sourceDirectory>
            <contentType>Html</contentType>
          </configuration>
          <executions>
            <execution>
              <phase>generate-sources</phase>
              <goals>
                <goal>generate</goal>
              </goals>
            </execution>
          </executions>
      </plugin>
''')

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }
}
