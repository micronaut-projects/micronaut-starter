package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SoySpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature views-soy contains links to micronaut docs'() {
        when:
        def output = generate(['views-soy'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/google/closure-templates")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#soy")
    }

    @Unroll
    void 'test gradle views-soy feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-soy'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-soy")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-soy feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-soy'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-soy</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
