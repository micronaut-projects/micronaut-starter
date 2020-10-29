package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class JUnitSpec extends BeanContextSpec {

    void "test junit with different languages"() {

        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([]), false).render().toString()

        then:
        template.contains("testRuntime(\"junit5\")")

        when:
        template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], Language.GROOVY, TestFramework.JUNIT), false).render().toString()

        then:
        template.contains("testRuntime(\"junit5\")")
        !template.contains("testAnnotationProcessor")

        when:
        template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], Language.KOTLIN, TestFramework.JUNIT), false).render().toString()

        then:
        template.contains("testRuntime(\"junit5\")")
        !template.contains("testAnnotationProcessor")
    }

}
