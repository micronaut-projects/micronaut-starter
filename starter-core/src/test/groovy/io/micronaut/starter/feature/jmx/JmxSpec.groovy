package io.micronaut.starter.feature.jmx

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JmxSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Unroll
    void 'test readme.md contains links to jmx and micronaut docs'() {
        when:
        def output = generate(['jmx'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-jmx/latest/guide/index.html")
    }


    @Unroll
    void 'test gradle jmx feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jmx'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.jmx:micronaut-jmx")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven jmx feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jmx'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.jmx</groupId>
      <artifactId>micronaut-jmx</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
