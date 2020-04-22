package io.micronaut.starter.feature.rss

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RssSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle rss feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['rss'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-rss"')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven rss feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['rss'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-rss</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

}
