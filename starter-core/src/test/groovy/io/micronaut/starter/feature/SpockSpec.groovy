package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class SpockSpec extends ApplicationContextSpec {

    @Unroll
    void "for #language Gradle applications groovy gradle plugin is applied if Spock is used"(Language language) {
        when:
        String template = gradleTemplate(language, [], ApplicationType.DEFAULT, buildProject(), BuildTool.GRADLE, TestFramework.SPOCK)

        then:
        template.contains('id("groovy")')

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }
}
