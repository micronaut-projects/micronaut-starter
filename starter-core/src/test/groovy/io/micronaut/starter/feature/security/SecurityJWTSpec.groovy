package io.micronaut.starter.feature.security

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class SecurityJWTSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature security-jwt contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['security-jwt'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html")
    }

    void "test dependencies added for security-jwt feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([SecurityJWT.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.security", "micronaut-security-jwt", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.security", "micronaut-security-processor", Scope.ANNOTATION_PROCESSOR, 'micronaut.security.version', true)

        where:
        buildTool << BuildTool.values()
    }

    void 'test security-jwt configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-jwt'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'bearer'
        commandContext.configuration.get('micronaut.security.token.jwt.signatures.secret.generator.secret'.toString()) == '${JWT_GENERATOR_SIGNATURE_SECRET:pleaseChangeThisSecretForANewOne}'
    }
}
