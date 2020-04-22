package io.micronaut.starter.feature.netflix

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RibbonSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle netflix-ribbon feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['netflix-ribbon'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.configuration:micronaut-netflix-ribbon")')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven netflix-ribbon feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['netflix-ribbon'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-netflix-ribbon</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    void 'test netflix-ribbon configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['netflix-ribbon'])

        then:
        commandContext.configuration.get('ribbon.VipAddress'.toString()) == 'test'
        commandContext.configuration.get('ribbon.ServerListRefreshInterval'.toString()) == 2000
    }

}
