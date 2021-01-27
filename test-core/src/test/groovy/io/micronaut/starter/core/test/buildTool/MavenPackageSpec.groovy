package io.micronaut.starter.core.test.buildTool

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Requires
import spock.lang.Retry
import spock.lang.Unroll
import spock.util.environment.Jvm

@Retry // sometimes CI gets connection failure/reset resolving dependencies from Maven central
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
    @Requires({ Jvm.current.java8 || Jvm.current.java11 })
    void 'test maven Docker Native packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven( "package -Dpackaging=docker-native", 30)

        then:
        output.contains("Using BASE_IMAGE: ghcr.io/graalvm/graalvm-ce:java11-21.0.0")

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
