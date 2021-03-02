package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.options.Language

class GroovySpec extends ApplicationContextSpec {

    void "for Groovy Gradle applications groovy gradle plugin is applied"() {
        when:
        String template = gradleTemplate(Language.GROOVY, [])

        then:
        template.contains('id("groovy")')
    }
}
