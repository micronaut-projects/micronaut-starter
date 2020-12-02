package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class VelocitySpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature views-velocity contains links to micronaut docs'() {
        when:
        def output = generate(['views-velocity'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains('https://velocity.apache.org')
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#velocity")
    }

    @Unroll
    void 'test gradle views-velocity feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-velocity'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-velocity")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-velocity feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-velocity'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-velocity</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
