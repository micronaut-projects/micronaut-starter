package io.micronaut.starter.feature.build.gradle

import io.micronaut.context.BeanContext
import io.micronaut.starter.build.Property
import io.micronaut.starter.feature.build.gradle.templates.annotationProcessors
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.feature.graalvm.GraalNativeImage
import io.micronaut.starter.feature.jdbc.Dbcp
import io.micronaut.starter.feature.jdbc.Hikari
import io.micronaut.starter.feature.jdbc.Tomcat
import io.micronaut.starter.feature.server.Jetty
import io.micronaut.starter.feature.server.Netty
import io.micronaut.starter.feature.server.Undertow
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class GradleSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

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

    void 'test graal-native-image feature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["graal-native-image"])).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graal-native-image"], Language.kotlin)).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), getFeatures(["graal-native-image"], Language.groovy)).render().toString()

        then:
        template.count('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")') == 1
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')
    }

    @Unroll
    void 'test jdbc feature #jdbcFeature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([jdbcFeature])).render().toString()

        then:
        template.contains("implementation \"io.micronaut.configuration:micronaut-${jdbcFeature}\"")

        where:
        jdbcFeature << ["jdbc-dbcp", "jdbc-hikari", "jdbc-tomcat"]
    }

    @Unroll
    void 'test server feature #serverFeature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([serverFeature])).render().toString()

        then:
        template.contains(dependency)

        where:
        serverFeature          | dependency
        "netty-server"         | 'implementation "io.micronaut:micronaut-http-server-netty"'
        "jetty-server"         | 'implementation "io.micronaut.servlet:micronaut-http-server-jetty"'
        "tomcat-server"        | 'implementation "io.micronaut.servlet:micronaut-http-server-tomcat"'
        "undertow-server"      | 'implementation "io.micronaut.servlet:micronaut-http-server-undertow"'
    }

}
