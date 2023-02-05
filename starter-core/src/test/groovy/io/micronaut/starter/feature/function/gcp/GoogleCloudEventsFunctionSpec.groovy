package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudEventsFunctionSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature google-cloud-function contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        def output = generate(ApplicationType.FUNCTION, options, ['google-cloud-function-cloudevents'])
        def build = output['build.gradle']
        def function = output['src/main/java/example/micronaut/Function.java']
        def readme = output['README.md']

        then:
        build
        build.contains('implementation("io.micronaut.gcp:micronaut-gcp-function-cloudevents")')
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
    }
}
