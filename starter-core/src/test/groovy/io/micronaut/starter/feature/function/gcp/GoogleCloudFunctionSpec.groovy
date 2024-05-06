package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.json.JacksonDatabindFeature
import io.micronaut.starter.feature.other.ShadePlugin
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Issue
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Subject

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    GoogleCloudFunction googleCloudFunction = new GoogleCloudFunction(new ShadePlugin(), new JacksonDatabindFeature())

    void 'test readme.md with feature google-cloud-function contains links to docs'() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_17),
                ['google-cloud-function']
        )
        String readme = output["README.md"]

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

    void "google-cloud-function does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !googleCloudFunction.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList() - [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
        description = applicationType.name
    }

    void "google-cloud-function supports #description"(ApplicationType applicationType, String description) {
        expect:
        googleCloudFunction.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
        description = applicationType.name
    }

    void 'test gradle google cloud function feature for language=#language'() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_17),
                ['google-cloud-function']
        )
        String readme = output["README.md"]

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

    @Issue("https://github.com/GoogleCloudPlatform/functions-framework-java/pull/32/files")
    void "for spock is required to add micronaut-servlet-core"() {
        when:
        BuildTool buildTool = BuildTool.GRADLE
        String build = new BuildBuilder(beanContext, buildTool)
                .features(['google-cloud-function'])
                .testFramework(TestFramework.SPOCK)
                .jdkVersion(JdkVersion.JDK_17)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, build)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-jackson-databind", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.servlet", "micronaut-servlet-core", Scope.TEST)
    }

    void "runtime for gradle and google-cloud-function"() {
        when:
        BuildTool buildTool = BuildTool.GRADLE
        String build = new BuildBuilder(beanContext, buildTool)
                .features(['google-cloud-function'])
                .testFramework(TestFramework.JUNIT)
                .jdkVersion(JdkVersion.JDK_17)
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, build)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-jackson-databind", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-server-netty", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)

        and:
        build.contains('runtime("google_function")')
    }

    void 'test gradle google cloud function feature for language=#language - raw function'() {
        when:
        Map<String, String> output = generate(
                ApplicationType.FUNCTION,
                new Options(language, BuildTool.GRADLE),
                ['google-cloud-function']
        )
        String build = output['build.gradle']
        String readme = output["README.md"]

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

    void 'test Google Cloud JDK support fails with #jdkVersion'() {
        when:
        generate(
                ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                ['google-cloud-function']
        )

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Google Cloud Function currently only supports JDK 11 and 17 -- https://cloud.google.com/functions/docs/concepts/java-runtime'

        where:
        jdkVersion << [JdkVersion.JDK_8, JdkVersion.JDK_21]
    }

    void 'test Google Cloud Function with graalvm is unsupported'() {
        when:
        generate(
            ApplicationType.DEFAULT,
            new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
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
                .jdkVersion(JdkVersion.JDK_17)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        if (buildTool.isGradle()) {
            assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http", Scope.COMPILE)
            assert !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http-test", Scope.TEST)

        } else if (buildTool == BuildTool.MAVEN) {
            assert verifier.hasDependency("com.google.cloud.functions", "functions-framework-api", Scope.COMPILE_ONLY)
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
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function-http-test", Scope.TEST)
        verifier.hasDependency("com.google.cloud.functions", "functions-framework-api", Scope.COMPILE_ONLY)
        if (buildTool.isGradle()) {
            assert verifier.hasDependency("com.google.cloud.functions", "functions-framework-api", Scope.TEST)
        }

        verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }
}
