package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class HandlebarsSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle views-handlebars feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['views-handlebars'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-handlebars")')
        template.contains('runtime "com.github.jknack:handlebars:4.1.2"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven views-handlebars feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['views-handlebars'], language), []).render().toString()

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
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

}
