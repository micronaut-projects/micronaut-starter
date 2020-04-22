package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CaffeineSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle cache-caffeine feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['cache-caffeine'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.cache:micronaut-cache-caffeine"')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven cache-caffeine feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['cache-caffeine'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-caffeine</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

}
