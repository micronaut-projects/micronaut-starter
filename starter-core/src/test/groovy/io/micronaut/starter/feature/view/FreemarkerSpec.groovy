package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class FreemarkerSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle views-freemarker feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['views-freemarker'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-freemarker")')
        template.contains('runtime("org.freemarker:freemarker:2.3.28")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-freemarker feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['views-freemarker'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views-freemarker</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.freemarker</groupId>
      <artifactId>freemarker</artifactId>
      <version>2.3.28</version>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
