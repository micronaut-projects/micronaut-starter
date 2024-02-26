package io.micronaut.starter.feature.security

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class SecuritySessionSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature security-session contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['security-session'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#session")
    }

    void 'test #buildTool security-session feature dependencies'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['security-session'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.security", "micronaut-security-session")
        verifier.hasDependency("io.micronaut.security", "micronaut-security-processor", Scope.ANNOTATION_PROCESSOR, 'micronaut.security.version', true)

        where:
        buildTool << BuildTool.values()
    }

    void 'test #buildTool security-session removes http-session feature'(Language language, BuildTool  buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['http-session', 'security-session'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("io.micronaut", "micronaut-session")
        verifier.hasDependency("io.micronaut.security", "micronaut-security-session")

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void 'test security-session configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['security-session'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'session'

        when:
        commandContext = buildGeneratorContext(['security-session', 'security-jwt'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'session'

        when:
        commandContext = buildGeneratorContext(['security-session', 'security-jwt', 'security-oauth2'])

        then:
        commandContext.configuration.get('micronaut.security.authentication') == 'session'
    }
}
