package io.micronaut.starter.feature.rxjava

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class RxJava1Spec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature rxjava1 contains links to micronaut docs'() {
        when:
        def output = generate(['rxjava1'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rxjava1/latest/guide/index.html")
    }

    @Subject
    @Shared
    RxJava1 rxJavaOne = beanContext.getBean(RxJava1)

    void "rxjava1 belongs to Reactive category"() {
        expect:
        Category.REACTIVE == rxJavaOne.category
    }

    @Unroll("feature rxjava1 works for application type: #applicationType")
    void "feature rxjava1 works for every type of application type"(ApplicationType applicationType) {
        expect:
        rxJavaOne.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature rxjava1 for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['rxjava1'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rxjava1</groupId>
      <artifactId>micronaut-rxjava1</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature rxjava1 for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['rxjava1'])
                .render()

        then:
        template.contains('implementation("io.micronaut.rxjava1:micronaut-rxjava1")')

        where:
        language << Language.values()
    }
}
