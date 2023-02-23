package io.micronaut.starter.feature.retry

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.build.gradle.MicronautGradleEnterprise
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.PendingFeature
import spock.lang.Unroll

class RetrySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @PendingFeature(reason = 'The Retry feature is for Micronaut 4, and should be visible for Starter 4.0.0')
    void "Retry feature is visible"() {
        expect:
        beanContext.getBean(Retry).isVisible()
    }

    void 'test readme.md with feature retry contains links to micronaut docs'() {
        when:
        def output = generate(['retry'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/#retry")
    }

    @Unroll
    void 'test gradle retry feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['retry'])
                .render()

        then:
        template.contains('implementation("io.micronaut:micronaut-retry")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven retry feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['retry'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-retry</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
