package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.lang.groovy.Groovy
import io.micronaut.starter.feature.lang.groovy.GroovyApplication
import io.micronaut.starter.feature.lang.java.Java
import io.micronaut.starter.feature.lang.java.JavaApplication
import io.micronaut.starter.feature.lang.kotlin.Kotlin
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication
import io.micronaut.starter.feature.test.Junit
import io.micronaut.starter.feature.test.KotlinTest
import io.micronaut.starter.feature.test.Spock
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.NameUtils
import spock.lang.Specification
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.annotationProcessors

class GradleSpec extends Specification {

    void "test settings.gradle"() {
        String template = settingsGradle.template(NameUtils.parse("abc.foo")).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
    }

    void "test gradle.properties"() {
        String template = gradleProperties.template([name: "Sally", age: "30"]).render().toString()

        expect:
        template.contains('name=Sally')
        template.contains('age=30')
    }

    void "test annotation processor dependencies"() {
        when:
        String template = annotationProcessors.template(new Features([new Java(new JavaApplication(), new Junit())])).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-inject-java"')
        template.contains('annotationProcessor "io.micronaut:micronaut-validation"')

        when:
        template = annotationProcessors.template(new Features([new Kotlin(new KotlinApplication(), new KotlinTest())])).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-inject-java"')
        template.contains('kapt "io.micronaut:micronaut-validation"')

        when:
        template = annotationProcessors.template(new Features([new Groovy(new GroovyApplication(), new Spock())])).render().toString()

        then:
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "io.micronaut:micronaut-inject-groovy"')
    }
}
