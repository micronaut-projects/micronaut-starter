package io.micronaut.starter.servlet

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.ZipUtil
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Issue
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class ZipCreateControllerSpec extends Specification {

    @Inject
    CreateClient client

    void "test default create app command"() {
        when:
        def bytes = client.createApp("test", Collections.emptyList(), null, null, null, null)

        then:
        ZipUtil.isZip(bytes)
    }

    void "test default create app with feature"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, null, null)

        then:
        ZipUtil.containsFile(bytes, "build.gradle.kts")
        !ZipUtil.containsFileWithContents(bytes, "build.gradle.kts", ':svm')
    }

    void "test create app with kotlin"() {
        when:
        def bytes = client.createApp("test", ['graalvm'], null, null, Language.KOTLIN, null)

        then:
        ZipUtil.containsFile(bytes, "Application.kt")
    }

    void "test create app with groovy"() {
        when:
        def bytes = client.createApp("test", ['flyway'], null, null, Language.GROOVY, null)

        then:
        ZipUtil.containsFile(bytes, "Application.groovy")
    }

    void "test create app with maven"() {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.MAVEN, null, Language.GROOVY, null)

        then:
        ZipUtil.containsFile(bytes, "pom.xml")
    }

    void "test create app with spock"() {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.GRADLE, TestFramework.SPOCK, Language.GROOVY, null)

        then:
        ZipUtil.containsFileWithContents(bytes, "build.gradle", "spock")
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/2321")
    void "test create app with jdk=#jdkVersion"(JdkVersion jdkVersion) {
        when:
        def bytes = client.createApp("test", ['flyway'], BuildTool.GRADLE_KOTLIN, TestFramework.JUNIT, Language.JAVA, jdkVersion)
        int jdk = jdkVersion.majorVersion

        then:
        ZipUtil.containsFileWithContents(bytes, "build.gradle.kts", "sourceCompatibility = JavaVersion.toVersion(\"${jdk}\")")
        ZipUtil.containsFileWithContents(bytes, "build.gradle.kts", "targetCompatibility = JavaVersion.toVersion(\"${jdk}\")")

        where:
        jdkVersion << MicronautJdkVersionConfiguration.SUPPORTED_JDKS
    }

    @Client('/create')
    static interface CreateClient {
        @Get(uri = "/default/{name}{?features,build,test,lang,javaVersion}", consumes = "application/zip")
        byte[] createApp(
                String name,
                @Nullable List<String> features,
                @Nullable BuildTool build,
                @Nullable TestFramework test,
                @Nullable Language lang,
                @Nullable JdkVersion javaVersion
        );
    }
}
