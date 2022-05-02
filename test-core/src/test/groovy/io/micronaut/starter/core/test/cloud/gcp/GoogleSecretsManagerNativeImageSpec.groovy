package io.micronaut.starter.core.test.cloud.gcp

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class GoogleSecretsManagerNativeImageSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-gcpfunction"
    }

    @Unroll
    def "verify gcp-secrets-manager and graalvm features builds native-image with #lang and #build"() {
        given:
        List<String> features = ['gcp-secrets-manager', 'graalvm']
        generateProject(lang, build, features)

        when:
        String output = executeBuild(build, "nativeCompile")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, build] << [[Language.JAVA, Language.KOTLIN], [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    def "verify gcp-secrets-manager and graalvm features builds native-image with #lang and maven"() {
        given:
        List<String> features = ['gcp-secrets-manager', 'graalvm']
        BuildTool build = BuildTool.MAVEN
        generateProject(lang, build, features)

        when:                                        //Kotlin & Maven take over 3 minutes to compile
        String output = executeMaven("package -Dpackaging=native-image", 250)

        then:
        output.contains("BUILD SUCCESS")

        where:
        lang << [Language.JAVA, Language.KOTLIN]
    }
}
