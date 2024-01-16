package io.micronaut.starter.feature.jaxrs

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Issue
import spock.lang.Unroll

class JaxRsSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jax-rs contains links to micronaut docs'() {
        when:
        def output = generate([JaxRs.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html")
    }

    @Unroll
    void 'test jax-rs with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([JaxRs.NAME, 'kapt'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, language, template)

        then:
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server")
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-processor", scope)

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven jax-rs feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([JaxRs.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server")
        verifier.hasAnnotationProcessor("io.micronaut.jaxrs", "micronaut-jaxrs-processor")

        and:
        if (language == Language.KOTLIN) {
            assert verifier.hasTestAnnotationProcessor("io.micronaut.jaxrs", "micronaut-jaxrs-processor")
        }
        where:
        language << Language.values()
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/2264")
    void "test maven jax-rs feature for Groovy doesn't duplicate annotation processors"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .features([JaxRs.NAME])
                .render()

        then:
        // duplicates were added for Groovy annotationProcessor scope, so avoid regression
        template.count('''\
    <dependency>
      <groupId>io.micronaut.jaxrs</groupId>
      <artifactId>micronaut-jaxrs-processor</artifactId>
      <scope>provided</scope>
    </dependency>
''') == 1
    }

}
