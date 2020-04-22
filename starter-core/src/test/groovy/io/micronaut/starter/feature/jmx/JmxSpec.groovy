package io.micronaut.starter.feature.jmx

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JmxSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle jmx feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['jmx'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.configuration:micronaut-jmx")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jmx feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['jmx'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-jmx</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
