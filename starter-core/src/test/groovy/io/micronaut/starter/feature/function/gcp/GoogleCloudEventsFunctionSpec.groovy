package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.aws.AmazonApiGatewayHttp
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Requires
import spock.lang.Subject

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudEventsFunctionSpec extends BeanContextSpec implements CommandOutputFixture {

    @Subject
    GoogleCloudEventsFunction googleCloudEventsFunction = beanContext.getBean(GoogleCloudEventsFunction)

    void "google-cloud-function-cloudevents supports function application type"() {
        expect:
        googleCloudEventsFunction.supports(ApplicationType.FUNCTION)
    }

    void "google-cloud-function-cloudevents does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !googleCloudEventsFunction.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION)
    }

    void 'test dependencies for #buildTool build with feature google-cloud-function'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.FUNCTION)
                .features(['google-cloud-function-cloudevents'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifyAll {
            verifier.hasDependency('io.micronaut.gcp', 'micronaut-gcp-function-cloudevents')
            verifier.hasDependency('io.micronaut.serde', 'micronaut-serde-api')
        }

        where:
        buildTool << BuildTool.values()
    }

    void 'test readme.md and function for #buildTool build with feature google-cloud-function'(BuildTool buildTool) {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_8)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, ['google-cloud-function-cloudevents'])
        String function = output['src/main/java/example/micronaut/Function.java']
        String readme = output['README.md']

        then:
        function
        function.contains('extends GoogleCloudEventsFunction')
        readme
        verifyAll {
            readme.contains('# Micronaut and Google Cloud Function')
            readme.contains('## Feature google-cloud-function-cloudevents documentation')
            readme.contains('https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#cloudEventsFunctions')
        }

        when:
        readme = readme.replaceFirst('## Feature google-cloud-function-cloudevents documentation', '')

        then:
        // make sure we didn't add docs more than once
        !readme.contains('## Feature google-cloud-function-cloudevents documentation')

        where:
        buildTool << BuildTool.values()
    }
}
