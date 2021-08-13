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

class RxJavaTwoSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature rxjava2 contains links to micronaut docs'() {
        when:
        def output = generate(['rxjava2'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rxjava2/snapshot/guide/index.html")
    }

    @Subject
    @Shared
    RxJavaThree rxJavaThree = beanContext.getBean(RxJavaThree)

    void "rxjava2 belongs to Reactive category"() {
        expect:
        Category.REACTIVE == rxJavaThree.category
    }

    @Unroll("feature rxjava2 works for application type: #applicationType")
    void "feature rxjava2 works for every type of application type"(ApplicationType applicationType) {
        expect:
        rxJavaThree.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature rxjava2 for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['rxjava2'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rxjava2</groupId>
      <artifactId>micronaut-rxjava2</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        and:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rxjava2</groupId>
      <artifactId>micronaut-rxjava2-http-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        and:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rxjava2</groupId>
      <artifactId>micronaut-rxjava2-http-server-netty</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature rxjava2 for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['rxjava2'])
                .render()

        then:
        template.contains('implementation("io.micronaut.rxjava2:micronaut-rxjava2")')
        template.contains('implementation("io.micronaut.rxjava2:micronaut-rxjava2-http-client")')
        template.contains('implementation("io.micronaut.rxjava2:micronaut-rxjava2-http-server-netty")')

        where:
        language << Language.values()
    }
}
