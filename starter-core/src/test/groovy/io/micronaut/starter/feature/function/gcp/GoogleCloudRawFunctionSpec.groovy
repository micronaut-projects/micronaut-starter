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
import spock.lang.Unroll

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    public static final String GROUP_ID_GOOGLE_CLOUD_FUNCTIONS = "com.google.cloud.functions"
    public static final String ARTIACT_ID_FUNCTIONS_FRAMEWORK_API = "functions-framework-api"

    void 'test readme.md with feature google-cloud-function contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_11)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, ['google-cloud-function'])
        String readme = output["README.md"]

        then:
        readme
        verifyAll {
            readme.contains("https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions")
            // don't add azure-function-http for ApplicationType.FUNCTION
            !readme.contains("## Feature google-cloud-function-http documentation")
        }

        when:
        readme = readme.replaceFirst("## Feature google-cloud-function documentation", "")

        then:
        // make sure we didn't add docs more than once
        !readme.contains("## Feature google-cloud-function documentation")
    }

    @Unroll
    void "google-cloud-function dependencies for aws-lambda function and #language and #buildTool"(Language language, BuildTool buildTool) {
        when:
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, new BuildBuilder(beanContext, buildTool)
                .features(['google-cloud-function'])
                .language(language)
                .applicationType(ApplicationType.FUNCTION)
                .jdkVersion(JdkVersion.JDK_11)
                .render())

        then:
        verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-function")
        if (buildTool.isGradle()) {
            assert verifier.hasDependency(GROUP_ID_GOOGLE_CLOUD_FUNCTIONS, ARTIACT_ID_FUNCTIONS_FRAMEWORK_API, Scope.COMPILE_ONLY)
            assert verifier.hasDependency(GROUP_ID_GOOGLE_CLOUD_FUNCTIONS, ARTIACT_ID_FUNCTIONS_FRAMEWORK_API, Scope.TEST)
        }

        where:
        language << Language.values()
        buildTool << BuildTool.values()
    }
}
