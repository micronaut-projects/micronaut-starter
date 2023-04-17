package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    GoogleCloudFunction googleCloudFunction = new GoogleCloudFunction()

    void 'test readme.md with feature google-cloud-function contains links to docs'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['google-cloud-function']
        )
        def readme = output["README.md"]

        then:
        readme
        verifyAll {
            readme.contains("https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions")
            readme.contains("https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#httpFunctions")
        }

        when:
        readme = readme.replaceFirst("## Feature google-cloud-function documentation","")
        readme = readme.replaceFirst("## Feature google-cloud-function-http documentation","")
        readme = readme.replaceFirst("# Micronaut and Google Cloud Function","")

        then:
        verifyAll {
            !readme.contains("## Feature google-cloud-function documentation")
            !readme.contains("## Feature google-cloud-function-http documentation")
            !readme.contains("# Micronaut and Google Cloud Function")
        }

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
        def readme = output["README.md"]

        then:
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

    void "runtime for gradle and google-cloud-function"() {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['google-cloud-function'])
                .testFramework(TestFramework.JUNIT)
                .jdkVersion(JdkVersion.JDK_11)
                .render()

        then:
        build.contains('runtime("google_function")')
        !build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function")')
        !build.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !build.contains('implementation("io.micronaut:micronaut-http-client")')

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
                new Options(language, BuildTool.GRADLE),
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
        output.containsKey(language.getDefaults().getTest().getSourcePath("/example/micronaut/Function", language))
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
        jdkVersion << [JdkVersion.JDK_8]
    }

    void 'test Google Cloud Function with #serialization is unsupported'() {
        when:
        generate(
                ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
                ['google-cloud-function',serialization]
        )

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Google Cloud Function does not currently support micronaut-serialization.'

        where:
        serialization << ['serialization-jackson', 'serialization-bson', 'serialization-jsonp']
    }

    void 'test Google Cloud Function with graalvm is unsupported'() {
        when:
        generate(
            ApplicationType.DEFAULT,
            new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11),
            ['google-cloud-function','graalvm']
        )

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Google Cloud Function is not supported for GraalVM. ' +
                'Consider Google Cloud Run for deploying GraalVM native images as docker containers.'
    }

    void 'test gcp-function-http feature for language=#language and buildTool=#buildTool'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.DEFAULT)
                .features(['google-cloud-function'])
                .language(language)
                .jdkVersion(JdkVersion.JDK_11)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        if (buildTool.isGradle()) {
            assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http", Scope.COMPILE)
            assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http-test", Scope.TEST)

        } else if (buildTool == BuildTool.MAVEN) {
            assert verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http", Scope.COMPILE)
            assert verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http-test", Scope.TEST)
        }
        assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }

    void 'test gcp-function feature for language=#language and buildTool=#buildTool'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.FUNCTION)
                .features(['google-cloud-function'])
                .language(language)
                .jdkVersion(JdkVersion.JDK_11)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http", Scope.COMPILE)
        assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http-test", Scope.TEST)
        assert verifier.hasDependency("com.google.cloud.functions", "functions-framework-api")
        assert verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }
}
