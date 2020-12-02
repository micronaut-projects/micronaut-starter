package io.micronaut.starter.feature.netflix

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ArchaiusSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle netflix-archaius feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['netflix-archaius'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.netflix:micronaut-netflix-archaius")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven netflix-archaius feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['netflix-archaius'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.netflix</groupId>
      <artifactId>micronaut-netflix-archaius</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
