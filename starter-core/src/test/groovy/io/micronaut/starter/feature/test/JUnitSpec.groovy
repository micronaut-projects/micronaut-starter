package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class JUnitSpec extends ApplicationContextSpec {

    void "test junit with different languages"() {

        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE).render()

        then:
        template.contains("testRuntime(\"junit5\")")

        when:
        template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(Language.GROOVY)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("testRuntime(\"junit5\")")
        !template.contains("testAnnotationProcessor")

        when:
        template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(Language.KOTLIN)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("testRuntime(\"junit5\")")
        !template.contains("testAnnotationProcessor")
    }

}
