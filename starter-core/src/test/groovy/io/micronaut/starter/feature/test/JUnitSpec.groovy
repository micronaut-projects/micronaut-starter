package io.micronaut.starter.feature.test

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class JUnitSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "test junit with different languages"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([])).render().toString()

        then:
        template.contains("""
    testAnnotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")
    testAnnotationProcessor "io.micronaut:micronaut-inject-java"
    testImplementation platform("io.micronaut:micronaut-bom:\$micronautVersion")
    testImplementation "org.junit.jupiter:junit-jupiter-api"
    testImplementation "io.micronaut.test:micronaut-test-junit5"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine"
""")

        when:
        template = buildGradle.template(buildProject(), getFeatures([], Language.groovy, TestFramework.junit)).render().toString()

        then:
        template.contains("""
    testImplementation platform("io.micronaut:micronaut-bom:\$micronautVersion")
    testImplementation "io.micronaut:micronaut-inject-groovy"
    testImplementation "org.junit.jupiter:junit-jupiter-api"
    testImplementation "io.micronaut.test:micronaut-test-junit5"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine"
""")
        !template.contains("testAnnotationProcessor")

        when:
        template = buildGradle.template(buildProject(), getFeatures([], Language.kotlin, TestFramework.junit)).render().toString()

        then:
        template.contains("""
    kaptTest platform("io.micronaut:micronaut-bom:\$micronautVersion")
    kaptTest "io.micronaut:micronaut-inject-java"
    testImplementation platform("io.micronaut:micronaut-bom:\$micronautVersion")
    testImplementation "org.junit.jupiter:junit-jupiter-api"
    testImplementation "io.micronaut.test:micronaut-test-junit5"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine"
""")
        !template.contains("testAnnotationProcessor")
    }

}
