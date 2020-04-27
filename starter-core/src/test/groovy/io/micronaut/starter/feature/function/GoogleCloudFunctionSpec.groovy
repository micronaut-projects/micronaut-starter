package io.micronaut.starter.feature.function

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.VersionInfo
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class GoogleCloudFunctionSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    GoogleCloudFunction googleCloudFunction = new GoogleCloudFunction()

    @Unroll
    void "google-cloud-function does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !googleCloudFunction.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList() - [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
        description = applicationType.name
    }

    @Unroll
    void "google-cloud-function supports #description"(ApplicationType applicationType, String description) {
        expect:
        googleCloudFunction.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
        description = applicationType.name
    }

    @Unroll
    void 'test gradle google cloud function feature for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language),
                ['google-cloud-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('compileOnly(enforcedPlatform("io.micronaut:micronaut-bom:$micronautVersion"))')
        build.contains('compileOnly("com.google.cloud.functions:functions-framework-api")')
        build.contains('invoker("com.google.cloud.functions.invoker:java-function-invoker:'+ VersionInfo.getDependencyVersion("google.function.invoker").getValue() +'")')
        build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function-http")')
        !build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function")')
        !build.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !build.contains('implementation("io.micronaut:micronaut-http-client")')
        output.containsKey("$srcDir/example/micronaut/HelloController.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/HelloFunctionTest.$extension".toString())
        output.get("$testSrcDir/example/micronaut/HelloFunctionTest.$extension".toString())
                .contains("HelloFunctionTest")
        readme?.contains("Micronaut and Google Cloud Function")
        readme?.contains(BuildTool.GRADLE.getJarDirectory())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'test gradle google cloud function feature for language=#language - raw function'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language),
                ['google-cloud-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('compileOnly(enforcedPlatform("io.micronaut:micronaut-bom:$micronautVersion"))')
        build.contains('compileOnly("com.google.cloud.functions:functions-framework-api")')
        build.contains('invoker("com.google.cloud.functions.invoker:java-function-invoker:'+ VersionInfo.getDependencyVersion("google.function.invoker").getValue() +'")')
        !build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function-http")')
        build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function")')
        !build.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !build.contains('implementation("io.micronaut:micronaut-http-client")')
        !output.containsKey("$srcDir/example/micronaut/HelloController.$extension".toString())
        !output.containsKey("$testSrcDir/example/micronaut/HelloFunctionTest.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/FunctionTest.$extension".toString())
        readme?.contains("Micronaut and Google Cloud Function")
        readme?.contains(BuildTool.GRADLE.getJarDirectory())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}
