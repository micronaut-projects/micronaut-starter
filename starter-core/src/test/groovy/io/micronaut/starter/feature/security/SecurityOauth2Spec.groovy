package io.micronaut.starter.feature.security

import io.micronaut.context.env.Environment
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class SecurityOauth2Spec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature security-oauth2 contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['security-oauth2'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#oauth")
    }

    void "test dependencies added for security-oauth2 feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([SecurityOAuth2.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.security", "micronaut-security-oauth2", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.security", "micronaut-security-processor", Scope.ANNOTATION_PROCESSOR, 'micronaut.security.version', true)

        where:
        buildTool << BuildTool.values()
    }

    void 'test #buildTool security-oauth2 feature dependencies'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['security-oauth2'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.security", "micronaut-security-oauth2")
        verifier.hasDependency("io.micronaut.security", "micronaut-security-processor", Scope.ANNOTATION_PROCESSOR, 'micronaut.security.version', true)

        where:
        buildTool << BuildTool.values()
    }

    void 'test security-oauth2 configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-oauth2'])

        then:
        commandContext.configuration.get("micronaut.security.authentication") == "cookie"
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-id")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-secret")
        !commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.openid.issuer")

        when:
        commandContext = buildGeneratorContext(['security-oauth2', 'security-jwt'])

        then:
        commandContext.configuration.get("micronaut.security.authentication") == "cookie"
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-id")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.client-secret")
        commandContext.getConfiguration(Environment.DEVELOPMENT).containsKey("micronaut.security.oauth2.clients.default.openid.issuer")
    }
}
