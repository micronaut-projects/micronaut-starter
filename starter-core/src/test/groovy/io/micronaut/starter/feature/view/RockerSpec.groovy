package io.micronaut.starter.feature.view

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.build.dependencies.StarterCoordinates
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class RockerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-rocker contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['views-rocker'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/fizzed/rocker")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#rocker")
    }

    void 'test #buildTool views-rocker feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['views-rocker'])
                .render()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-rocker")')
        if (buildTool == BuildTool.GRADLE) {
        assert template.contains('''\
rocker {
    configurations {
        main {
            templateDir = file('src/rocker')
            outputDir = file('src/generated/rocker')
        }
    }
}
''')
        } else if (buildTool == BuildTool.GRADLE_KOTLIN) {
            assert template.contains('''\
rocker {
    configurations {
        create("main") {
            templateDir.set(file("src/rocker"))
            outputDir.set(file("src/generated/rocker"))
        }
    }
}
''')
        }

        when:
        String pluginId = 'nu.studer.rocker'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        where:
        [language, buildTool] << [Language.values(), BuildTool.valuesGradle()].combinations()
    }

    void 'test maven views-rocker feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['views-rocker'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.views", "micronaut-views-rocker", Scope.COMPILE)
        template.contains("""
      <plugin>
        <groupId>com.fizzed</groupId>
        <artifactId>rocker-maven-plugin</artifactId>
        <version>${StarterCoordinates.ROCKER_MAVEN_PLUGIN.version}</version>
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

