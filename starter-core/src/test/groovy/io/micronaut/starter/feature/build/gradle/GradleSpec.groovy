package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.build.Property
import io.micronaut.starter.feature.build.gradle.templates.annotationProcessors
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.options.Language

class GradleSpec extends BeanContextSpec {

    void "test settings.gradle"() {
        String template = settingsGradle.template(buildProject()).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
    }

    void "test gradle.properties"() {
        String template = gradleProperties.template([new Property() {
            String key = "name"
            String value = "Sally"
        }, new Property() {
            String key = "age"
            String value = "30"
        }]).render().toString()

        expect:
        template.contains('name=Sally')
        template.contains('age=30')
    }

    void "test annotation processor dependencies"() {
        when:
        String template = annotationProcessors.template(getFeatures([])).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-inject-java"')
        template.contains('annotationProcessor "io.micronaut:micronaut-validation"')

        when:
        template = annotationProcessors.template(getFeatures([], Language.kotlin)).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-inject-java"')
        template.contains('kapt "io.micronaut:micronaut-validation"')

        when:
        template = annotationProcessors.template(getFeatures([], Language.groovy)).render().toString()

        then:
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "io.micronaut:micronaut-inject-groovy"')
    }

}
