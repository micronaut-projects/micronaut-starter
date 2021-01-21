package io.micronaut.starter.feature.function.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature google-cloud-function contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        def output = generate(ApplicationType.FUNCTION, options, ['google-cloud-function'])
        def readme = output["README.md"]

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

}
