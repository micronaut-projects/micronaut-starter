package io.micronaut.starter.feature.build.gradlekotlin

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradlekotlin.templates.annotationProcessors
import io.micronaut.starter.feature.build.gradlekotlin.templates.buildGradleKts
import io.micronaut.starter.feature.build.gradlekotlin.templates.settingsGradleKts
import io.micronaut.starter.feature.build.gradlekotlin.templates.versionsKt
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class GradleKotlinSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "test settings.gradle.kts"() {
        String template = settingsGradleKts.template(buildProject()).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
    }

    void "test Versions.kr"() {
        when:
        String template = versionsKt.template(buildProject(), getFeatures([], Language.kotlin, TestFramework.kotlintest, BuildTool.gradleKotlin)).render().toString()

        def expectation = """\
            |const val jvmVersion = "${VersionInfo.getJdkVersion()}"
            |const val micronautVersion = "1.3.4"
            |const val kotlinVersion = "1.3.50"
            |const val jacksonKtModuleVersion = "2.9.8"
            |const val logbackVersion = "1.2.3"
            |const val mockkVersion = "1.9.3"
            |const val kotlintestJunit5RunnerVersion = "3.3.2"
            |const val shadowJarVersion = "5.2.0"
            |""".stripMargin()
        then:
        template.contains(expectation)
    }

    void "test annotation processor dependencies"() {
        when:
        String template = annotationProcessors.template(getFeatures([], Language.java, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains('annotationProcessor(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('annotationProcessor("io.micronaut:micronaut-inject-java")')
        template.contains('annotationProcessor("io.micronaut:micronaut-validation")')

        when:
        template = annotationProcessors.template(getFeatures([], Language.kotlin, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains('kapt(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('kapt("io.micronaut:micronaut-inject-java")')
        template.contains('kapt("io.micronaut:micronaut-validation")')

        when:
        template = annotationProcessors.template(getFeatures([], Language.groovy, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("io.micronaut:micronaut-inject-groovy")')
    }

    void "test junit with different languages"() {
        when:
        String template = buildGradleKts.template(buildProject(), getFeatures([], Language.java, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains("""
    testAnnotationProcessor(platform("io.micronaut:micronaut-bom:\$micronautVersion"))
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation(platform("io.micronaut:micronaut-bom:\$micronautVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
""")

        when:
        template = buildGradleKts.template(buildProject(), getFeatures([], Language.groovy, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains("""
    testImplementation(platform("io.micronaut:micronaut-bom:\$micronautVersion"))
    testImplementation("io.micronaut:micronaut-inject-groovy")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
""")
        !template.contains("testAnnotationProcessor")

        when:
        template = buildGradleKts.template(buildProject(), getFeatures([], Language.kotlin, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains("""
    kaptTest(platform("io.micronaut:micronaut-bom:\$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation(platform("io.micronaut:micronaut-bom:\$micronautVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
""")
        !template.contains("testAnnotationProcessor")
    }

    void 'test graal-native-image feature'() {
        when:
        String template = buildGradleKts.template(buildProject(), getFeatures(["graal-native-image"], Language.java, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains('annotationProcessor(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('annotationProcessor("io.micronaut:micronaut-graal")')
        template.contains('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')

        when:
        template = buildGradleKts.template(buildProject(), getFeatures(["graal-native-image"], Language.kotlin, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains('kapt(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('kapt("io.micronaut:micronaut-graal")')
        template.contains('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))')
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')

        when:
        template = buildGradleKts.template(buildProject(), getFeatures(["graal-native-image"], Language.groovy, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.count('compileOnly(platform("io.micronaut:micronaut-bom:\$micronautVersion"))') == 1
        template.contains('compileOnly("org.graalvm.nativeimage:svm")')
    }

    @Unroll
    void 'test jdbc feature #jdbcFeature'() {
        when:
        String template = buildGradleKts.template(buildProject(), getFeatures([jdbcFeature], Language.java, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains("implementation(\"io.micronaut.configuration:micronaut-${jdbcFeature}\")")

        where:
        jdbcFeature << ["jdbc-dbcp", "jdbc-hikari", "jdbc-tomcat"]
    }

    @Unroll
    void 'test server feature #serverFeature'() {
        when:
        String template = buildGradleKts.template(buildProject(), getFeatures([serverFeature], Language.java, TestFramework.junit, BuildTool.gradleKotlin)).render().toString()

        then:
        template.contains(dependency)

        where:
        serverFeature          | dependency
        "netty-server"         | 'implementation("io.micronaut:micronaut-http-server-netty")'
        "jetty-server"         | 'implementation("io.micronaut.servlet:micronaut-http-server-jetty")'
        "tomcat-server"        | 'implementation("io.micronaut.servlet:micronaut-http-server-tomcat")'
        "undertow-server"      | 'implementation("io.micronaut.servlet:micronaut-http-server-undertow")'
    }

}
