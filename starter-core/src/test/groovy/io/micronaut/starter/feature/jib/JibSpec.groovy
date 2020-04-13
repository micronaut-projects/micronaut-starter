package io.micronaut.starter.feature.jib

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class JibSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle jib feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['jib'], language)).render().toString()

        then:
        template.contains('id "com.google.cloud.tools.jib" version "2.1.0"')
        template.contains("jib.to.image = 'gcr.io/foo/jib-image")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven asciidoctor feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['jib'], language), []).render().toString()

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
        language << [Language.java, Language.kotlin, Language.groovy]
    }

}
