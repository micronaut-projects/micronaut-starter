package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class SpockSpec extends ApplicationContextSpec {

    @Unroll
    void "for #language Gradle applications groovy gradle plugin is applied if Spock is used"(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains('id("groovy")')

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }
}
