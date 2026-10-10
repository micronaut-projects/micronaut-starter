package io.micronaut.starter.feature.camel

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Unroll

class CamelSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature camel contains links to the docs'() {
        when:
        Map<String, String> output = generate(['camel'])
        String readme = output["README.md"]

        then:
        readme.contains("https://micronaut-projects.github.io/micronaut-camel/latest/guide/index.html")
        readme.contains("https://camel.apache.org/manual/")
    }

    void 'test camel feature configures the Camel context name'() {
        when:
        Map<String, String> output = generate(['camel'])

        then:
        output["src/main/resources/application.properties"].contains("camel.main.name")
    }

    @Unroll
    void 'test gradle camel feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['camel'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, language, template)

        then:
        verifier.hasDependency("io.micronaut.camel", "micronaut-camel-core", Scope.COMPILE)
        verifier.hasAnnotationProcessor("io.micronaut.camel", "micronaut-camel-processor")
        verifier.hasDependency("io.micronaut.camel", "micronaut-camel-test", Scope.TEST)
        verifier.hasDependency("org.apache.camel", "camel-direct", Scope.COMPILE)

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    @Unroll
    void 'test maven camel feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['camel'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.camel", "micronaut-camel-core", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.camel", "micronaut-camel-test", Scope.TEST)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void 'test camel-http feature adds camel and the http module'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['camel-http'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)

        then:
        verifier.hasDependency("io.micronaut.camel", "micronaut-camel-http", Scope.COMPILE)
        verifier.hasDependency("org.apache.camel", "camel-rest", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.camel", "micronaut-camel-core", Scope.COMPILE)
    }
}
