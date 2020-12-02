package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GoogleCloudFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    GoogleCloudFunction googleCloudFunction = new GoogleCloudFunction()

    void 'test readme.md with feature google-cloud-function contains links to micronaut docs'() {
        when:
        def output = generate(
            ApplicationType.DEFAULT,
            new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
            ['google-cloud-function']
        )
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#cloudFunction")

        where:
        language << Language.values().toList()
    }

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
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['google-cloud-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('runtime("google_function")')
        !build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function")')
        !build.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !build.contains('implementation("io.micronaut:micronaut-http-client")')
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())
        output.containsKey("$srcDir/example/micronaut/FooController.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
        output.get("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
                .contains("FooFunctionTest")
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
        !build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function-http")')
        build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function")')
        !build.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !build.contains('implementation("io.micronaut:micronaut-http-client")')
        !output.containsKey("$srcDir/example/micronaut/FooController.$extension".toString())
        !output.containsKey("$testSrcDir/example/micronaut/FooFunctionTest.$extension".toString())
        !output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())
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

    void 'test Google Cloud does not support JDK < 11'() {
        when:
        generate(
            ApplicationType.DEFAULT,
            new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
            ['google-cloud-function']
        )

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Google Cloud Function needs at least JDK 11'

        where:
        jdkVersion << [JdkVersion.JDK_8, JdkVersion.JDK_9, JdkVersion.JDK_10]
    }
}
