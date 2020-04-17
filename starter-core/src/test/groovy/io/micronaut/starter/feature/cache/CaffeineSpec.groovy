package io.micronaut.starter.feature.cache

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CaffeineSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle cache-caffeine feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['cache-caffeine'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.cache:micronaut-cache-caffeine"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven cache-caffeine feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['cache-caffeine'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-caffeine</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

}
