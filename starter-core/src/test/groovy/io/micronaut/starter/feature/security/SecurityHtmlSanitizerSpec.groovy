package io.micronaut.starter.feature.security

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class SecurityHtmlSanitizerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature security-html-sanitizer contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['security-html-sanitizer'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#htmlSanitizer")
    }

    void "test dependencies added for security-html-sanitizer feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['security-html-sanitizer'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.security", "micronaut-security-html-sanitizer", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.security", "micronaut-security-processor", Scope.ANNOTATION_PROCESSOR, 'micronaut.security.version', true)

        where:
        buildTool << BuildTool.values()
    }
}
