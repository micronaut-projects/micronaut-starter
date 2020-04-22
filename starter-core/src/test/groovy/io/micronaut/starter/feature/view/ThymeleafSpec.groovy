package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ThymeleafSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle views-thymeleaf feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['views-thymeleaf'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-thymeleaf")')
        template.contains('runtime "org.thymeleaf:thymeleaf:3.0.11.RELEASE"')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven views-thymeleaf feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['views-thymeleaf'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views-thymeleaf</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf</artifactId>
      <version>3.0.11.RELEASE</version>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

}
