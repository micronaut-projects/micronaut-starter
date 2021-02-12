package io.micronaut.starter.feature.jib

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.GradleDsl
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JibSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle jib feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jib'], language) , new GradleBuild()).render().toString()

        then:
        template.contains('id("com.google.cloud.tools.jib")')
        template.contains("image = \"gcr.io/myapp/jib-image\"")

        where:
        language << Language.values().toList()
    }

}
