package io.micronaut.starter.feature.rxjava

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class RxJavaThreeSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature rxjava3 contains links to micronaut docs'() {
        when:
        def output = generate(['rxjava3'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rxjava3/snapshot/guide/index.html")
    }

    @Subject
    @Shared
    RxJavaThree rxJavaThree = beanContext.getBean(RxJavaThree)

    void "rxjava3 belongs to Reactive category"() {
        expect:
        Category.REACTIVE == rxJavaThree.category
    }

    @Unroll("feature rxjava3 works for application type: #applicationType")
    void "feature rxjava3 works for every type of application type"(ApplicationType applicationType) {
        expect:
        rxJavaThree.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature rxjava3 for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['rxjava3'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.rxjava3</groupId>
      <artifactId>micronaut-rxjava3</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature rxjava3 for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['rxjava3'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.rxjava3:micronaut-rxjava3")')

        where:
        language << Language.values()
    }
}
