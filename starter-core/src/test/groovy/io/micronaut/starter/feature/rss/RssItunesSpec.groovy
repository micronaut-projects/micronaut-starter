package io.micronaut.starter.feature.rss

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RssItunesSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle rss-itunes-podcast feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['rss-itunes-podcast'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-itunespodcast"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven rss-itunes-podcast feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['rss-itunes-podcast'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-itunespodcast</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

}
