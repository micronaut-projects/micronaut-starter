package io.micronaut.starter.core.test.buildTool

import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import spock.lang.IgnoreIf
import spock.lang.Unroll

class MavenPackageSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "maven"
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
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
        lang << Language.values()
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
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
        lang << Language.values()
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void 'test maven Docker Native packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven( "package -Dpackaging=docker-native -Pgraalvm")

        then:
        output.contains("Successfully tagged foo:latest")
        output.contains("BUILD SUCCESS")

        where:
        lang << Language.values().findAll { GraalVMFeatureValidator.supports(it) }
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    @Unroll
    void 'test native-image packaging for #lang'(Language lang) {
        given:
        generateProject(lang, BuildTool.MAVEN, [])

        when:
        String output = executeMaven( "package -Dpackaging=native-image")

        then:
        output.contains("GraalVM Native Image: Generating 'foo' (executable)...")
        output.contains("BUILD SUCCESS")

        where:
        lang << Language.values().findAll { GraalVMFeatureValidator.supports(it) }
    }

}
