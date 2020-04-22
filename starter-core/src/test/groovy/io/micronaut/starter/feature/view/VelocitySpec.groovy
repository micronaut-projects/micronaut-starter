package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VelocitySpec extends BeanContextSpec {

    @Unroll
    void 'test gradle views-velocity feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['views-velocity'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-views-velocity")')
        template.contains('runtime "org.apache.velocity:velocity-engine-core:2.0"')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven views-velocity feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['views-velocity'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-views-velocity</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.apache.velocity</groupId>
      <artifactId>velocity-engine-core</artifactId>
      <version>2.0</version>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

}
