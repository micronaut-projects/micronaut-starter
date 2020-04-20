package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class InfinispanSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle cache-infinispan feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['cache-infinispan'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.cache:micronaut-cache-infinispan"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven cache-infinispan feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['cache-infinispan'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-infinispan</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    void 'test cache-infinispan configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cache-infinispan'])

        then:
        commandContext.configuration.get('infinispan.client.hotrod.server.host'.toString()) == 'infinispan.example.com'
        commandContext.configuration.get('infinispan.client.hotrod.server.port'.toString()) == 10222
    }

}
