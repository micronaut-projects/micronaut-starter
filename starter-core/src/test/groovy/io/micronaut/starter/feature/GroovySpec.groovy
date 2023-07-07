package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class GroovySpec extends ApplicationContextSpec {

    void "for Groovy Gradle applications groovy gradle plugin is applied"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(Language.GROOVY)
                .render()

        then:
        template.contains('id("groovy")')
    }

    void 'test groovy with Maven applies gmavenplus plugin'() {
        given:
        Language language = Language.GROOVY
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .render()

        then:
        template.contains('''\
      <plugin>
        <groupId>org.codehaus.gmavenplus</groupId>
        <artifactId>gmavenplus-plugin</artifactId>
      </plugin>
''')
    }
}
