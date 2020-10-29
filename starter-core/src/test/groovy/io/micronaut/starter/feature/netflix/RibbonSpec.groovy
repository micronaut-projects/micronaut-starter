package io.micronaut.starter.feature.netflix

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class RibbonSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature netflix-ribbon contains links to micronaut docs'() {
        when:
        def output = generate(['netflix-ribbon'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#netflixRibbon")
    }

    @Unroll
    void 'test gradle netflix-ribbon feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['netflix-ribbon'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.netflix:micronaut-netflix-ribbon")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven netflix-ribbon feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['netflix-ribbon'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.netflix</groupId>
      <artifactId>micronaut-netflix-ribbon</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test netflix-ribbon configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['netflix-ribbon'])

        then:
        commandContext.configuration.get('ribbon.VipAddress'.toString()) == 'test'
        commandContext.configuration.get('ribbon.ServerListRefreshInterval'.toString()) == 2000
    }

}
