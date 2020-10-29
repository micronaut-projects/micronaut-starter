package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CaffeineSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature cache-caffeine contains links to micronaut docs'() {
        when:
        def output = generate(['cache-caffeine'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/ben-manes/caffeine")
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle cache-caffeine feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['cache-caffeine'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.cache:micronaut-cache-caffeine")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven cache-caffeine feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['cache-caffeine'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-caffeine</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
