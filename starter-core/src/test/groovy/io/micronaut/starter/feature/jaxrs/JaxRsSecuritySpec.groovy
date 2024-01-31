package io.micronaut.starter.feature.jaxrs

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier

import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Issue
import spock.lang.Unroll

class JaxRsSecuritySpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature jax-rs-security contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['security', JaxRs.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html")
    }

    @Unroll
    void 'test jax-rs-security with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['security', JaxRs.NAME, 'kapt'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, language, template)

        then:
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server-security")
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server")
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-processor", scope)

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test jax-rs-security is not with Gradle if none SecurityFeature selected'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([JaxRs.NAME])
                .language(Language.JAVA)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)

        then:
        !verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server-security")
    }

    void 'test maven jax-rs-security feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['security', JaxRs.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server-security")
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server")
        verifier.hasAnnotationProcessor("io.micronaut.jaxrs", "micronaut-jaxrs-processor")

        and:
        if (language == Language.KOTLIN) {
            assert verifier.hasTestAnnotationProcessor("io.micronaut.jaxrs", "micronaut-jaxrs-processor")
        } else {
            assert verifier.hasExclusion("io.micronaut.jaxrs", "micronaut-jaxrs-processor",
                    "io.micronaut", "micronaut-inject", Scope.ANNOTATION_PROCESSOR)
        }

        where:
        language << Language.values()
    }
}
