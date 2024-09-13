package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature google-cloud-function contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
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

    void 'test Google Cloud Function Maven plugin is applied for feature google-cloud-function'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_21)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, ['google-cloud-function'])
        String pom = output["pom.xml"]

        then:
        pom.contains("<groupId>com.google.cloud.functions</groupId>")
        pom.contains("<artifactId>function-maven-plugin</artifactId>")
    }

}
