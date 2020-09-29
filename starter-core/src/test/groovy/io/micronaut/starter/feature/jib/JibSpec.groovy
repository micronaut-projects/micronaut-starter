package io.micronaut.starter.feature.jib

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JibSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle jib feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jib'], language)).render().toString()

        then:
        template.contains('id "com.google.cloud.tools.jib"')
        template.contains("jib.to.image = 'gcr.io/foo/jib-image")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jib feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jib'], language), []).render().toString()

        then:
        template.contains("""
      <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <configuration>
          <to>
            <image>gcr.io/foo/jib-image</image>
          </to>
        </configuration>
      </plugin>
""")

        where:
        language << Language.values().toList()
    }

}
