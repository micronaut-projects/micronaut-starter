package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RockerSpec extends BeanContextSpec implements CommandOutputFixture {

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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-rocker'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-rocker")')
        template.contains('id("com.fizzed.rocker") version "1.3.0"')
        template.contains("""
sourceSets {
    main {
        rocker {
            srcDir("src/main/resources")
        }
    }
}
""")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-rocker feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-rocker'], language), []).render().toString()

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
