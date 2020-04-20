package io.micronaut.starter.feature.netflix

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RibbonSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle netflix-ribbon feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['netflix-ribbon'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-netflix-ribbon"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
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
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test netflix-ribbon configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['netflix-ribbon'])

        then:
        commandContext.configuration.get('ribbon.VipAddress'.toString()) == 'test'
        commandContext.configuration.get('ribbon.ServerListRefreshInterval'.toString()) == 2000
    }

}
