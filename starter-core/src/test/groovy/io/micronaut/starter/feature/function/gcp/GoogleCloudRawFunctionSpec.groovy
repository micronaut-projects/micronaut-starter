package io.micronaut.starter.feature.function.gcp

import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Requires

@Requires({ jvm.current.isJava11Compatible() })
class GoogleCloudRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature google-cloud-function contains links to micronaut docs'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(JdkVersion.JDK_8)
                .features(['google-cloud-function'])
                .build()
        when:
        Map<String, String> output = generate(options)
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

}
