package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo

class JUnitSpec extends BeanContextSpec {

    void "test junit with different languages"() {
        given:
        def policy = VersionInfo.isMicronautSnapshot() ? "enforcedPlatform" : "platform"

        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([])).render().toString()

        then:
        template.contains("""
    testAnnotationProcessor($policy("io.micronaut:micronaut-bom:\$micronautVersion"))
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation($policy("io.micronaut:micronaut-bom:\$micronautVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
""")

        when:
        template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], Language.GROOVY, TestFramework.JUNIT)).render().toString()

        then:
        template.contains("""
    testImplementation($policy("io.micronaut:micronaut-bom:\$micronautVersion"))
    testImplementation("io.micronaut:micronaut-inject-groovy")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
""")
        !template.contains("testAnnotationProcessor")

        when:
        template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], Language.KOTLIN, TestFramework.JUNIT)).render().toString()

        then:
        template.contains("""
    kaptTest($policy("io.micronaut:micronaut-bom:\$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation($policy("io.micronaut:micronaut-bom:\$micronautVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
""")
        !template.contains("testAnnotationProcessor")
    }

}
