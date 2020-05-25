package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class HandlebarsSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature views-handlebars contains links to micronaut docs'() {
        when:
        def output = generate(['views-handlebars'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://jknack.github.io/handlebars.java")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#handlebars")
    }

    @Unroll
    void 'test gradle views-handlebars feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-handlebars'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-handlebars")')
        template.contains('runtime("com.github.jknack:handlebars:4.1.2")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-handlebars feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-handlebars'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views-handlebars</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.github.jknack</groupId>
      <artifactId>handlebars</artifactId>
      <version>4.1.2</version>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
