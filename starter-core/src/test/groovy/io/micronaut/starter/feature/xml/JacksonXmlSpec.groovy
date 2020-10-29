package io.micronaut.starter.feature.xml

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JacksonXmlSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jackson-xml contains links to micronaut docs'() {
        when:
        def output = generate(['jackson-xml'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/FasterXML/jackson-dataformat-xml")
        readme.contains("https://micronaut-projects.github.io/micronaut-jackson-xml/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle jackson-xml feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jackson-xml'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.xml:micronaut-jackson-xml")')

        where:
        language << Language.values().toList().toList()
    }

    @Unroll
    void 'test maven jackson-xml feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['jackson-xml'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.xml</groupId>
      <artifactId>micronaut-jackson-xml</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList().toList()
    }

}
