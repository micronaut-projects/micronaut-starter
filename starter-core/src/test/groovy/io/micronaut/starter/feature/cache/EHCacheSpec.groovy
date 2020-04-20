package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class EHCacheSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle cache-ehcache feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['cache-ehcache'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.cache:micronaut-cache-ehcache"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven cache-ehcache feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['cache-ehcache'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-ehcache</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test cache-ehcache configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['cache-ehcache'])

        then:
        commandContext.configuration.get('micronaut.caches.my-cache.maximumSize'.toString()) == 20
    }

}
