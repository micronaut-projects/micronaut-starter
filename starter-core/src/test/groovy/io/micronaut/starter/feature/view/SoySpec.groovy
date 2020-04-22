package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SoySpec extends BeanContextSpec {

    @Unroll
    void 'test gradle views-soy feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['views-soy'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-soy")')
        template.contains('implementation("com.google.template:soy:2019-09-03")')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven views-soy feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['views-soy'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views-soy</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.template</groupId>
      <artifactId>soy</artifactId>
      <version>2019-09-03</version>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

}
