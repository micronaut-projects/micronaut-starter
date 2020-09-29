package io.micronaut.starter.feature.asciidoctor

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AsciidoctorSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle asciidoctor feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['asciidoctor'], language)).render().toString()

        then:
        template.contains('id "org.asciidoctor.jvm.convert"')
        template.contains("apply from: 'gradle/asciidoc.gradle'")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven asciidoctor feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['asciidoctor'], language), []).render().toString()

        then:
        template.contains("""
        <groupId>org.asciidoctor</groupId>
        <artifactId>asciidoctor-maven-plugin</artifactId>
        <version>\${asciidoctor.maven.plugin.version}</version>
        <dependencies>
          <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj</artifactId>
            <version>\${asciidoctorj.version}</version>
          </dependency>
          <dependency>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctorj-diagram</artifactId>
            <version>\${asciidoctorj.diagram.version}</version>
          </dependency>
        </dependencies>
""")

        where:
        language << Language.values().toList()
    }

}
