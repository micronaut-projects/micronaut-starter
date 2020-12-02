package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ThymeleafSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-thymeleaf contains links to micronaut docs'() {
        when:
        def output = generate(['views-thymeleaf'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://www.thymeleaf.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#thymeleaf")
    }

    @Unroll
    void 'test gradle views-thymeleaf feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-thymeleaf'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-thymeleaf")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-thymeleaf feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-thymeleaf'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-thymeleaf</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
