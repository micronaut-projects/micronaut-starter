package io.micronaut.starter.feature.email

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JavaMailFeatureSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Unroll
    void 'test #buildTool email-javamail feature for language=#language'(
    Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['email-javamail'])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.email", "micronaut-email-javamail")
        verifier.hasDependency("org.eclipse.angus", "angus-mail", Scope.RUNTIME)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void 'README.md contains third party docs link'() {
        when:
        Map<String, String> output = generate(['email-javamail'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://jakartaee.github.io/mail-api/")
    }
}
