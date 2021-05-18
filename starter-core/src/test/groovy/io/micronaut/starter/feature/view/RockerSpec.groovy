package io.micronaut.starter.feature.view

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RockerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-rocker contains links to micronaut docs'() {
        when:
        def output = generate(['views-rocker'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/fizzed/rocker")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#rocker")
    }

    @Unroll
    void 'test gradle views-rocker feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['views-rocker'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-rocker")')
        template.contains('''\
sourceSets {
    main {
        rocker {
            srcDir("src/main/resources")
        }
    }
}
''')

        when:
        String pluginId = 'com.fizzed.rocker'
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
    void 'test maven views-rocker feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-rocker'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-rocker</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
      <plugin>
        <groupId>com.fizzed</groupId>
        <artifactId>rocker-maven-plugin</artifactId>
        <version>1.3.0</version>
        <executions>
          <execution>
            <id>generate-rocker-templates</id>
            <phase>generate-sources</phase>
            <goals>
              <goal>generate</goal>
            </goals>
            <configuration>
              <templateDirectory>src/main/resources</templateDirectory>
            </configuration>
          </execution>
        </executions>
      </plugin>
""")

        where:
        language << Language.values().toList()
    }
}

