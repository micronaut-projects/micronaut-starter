package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RockerSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle views-rocker feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['views-rocker'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-rocker")')
        template.contains('id "com.fizzed.rocker" version "1.2.3"')
        template.contains("""
sourceSets {
    main {
        rocker {
            srcDir('src/main/resources')
        }
    }
}
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven views-rocker feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['views-rocker'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views-rocker</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
      <plugin>
        <groupId>com.fizzed</groupId>
        <artifactId>rocker-maven-plugin</artifactId>
        <version>1.2.3</version>
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
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

}
