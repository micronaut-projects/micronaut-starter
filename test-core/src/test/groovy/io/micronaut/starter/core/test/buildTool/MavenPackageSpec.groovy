package io.micronaut.starter.core.test.buildTool

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class MavenPackageSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "maven"
    }

    @Unroll
    void 'test maven JAR packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven("package")

        then:
        output.contains("Replacing original artifact with shaded artifact")
        output.contains("BUILD SUCCESS")

        where:
        lang << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven Docker packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven("package -Dpackaging=docker")

        then:
        output.contains("Built image to Docker daemon")
        output.contains("BUILD SUCCESS")

        where:
        lang << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }


    @Unroll
    void 'test maven Docker Native packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven( "package -Dpackaging=docker-native", 30)

        then:
        output.contains("Using BASE_IMAGE: oracle/graalvm-ce:20.2.0-java11")

        where:
        lang << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test native-image packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven( "package -Dpackaging=native-image")

        then:
        output.contains("org.graalvm.nativeimage:native-image-maven-plugin")

        where:
        lang << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

}
