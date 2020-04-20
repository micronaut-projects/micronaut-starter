package io.micronaut.starter.feature.externalconfig

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class KubernetesSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle kubernetes feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['kubernetes'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.kubernetes:micronaut-kubernetes-discovery-client"')
        template.contains('implementation "io.micronaut:micronaut-management"')
        template.contains('id "com.google.cloud.tools.jib" version "2.1.0"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    @Unroll
    void 'test maven kubernetes feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['kubernetes'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    void 'test kubernetes configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['kubernetes'])

        then:
        commandContext.bootstrapConfig.get('micronaut.application.name'.toString()) == 'foo'
        commandContext.bootstrapConfig.get('micronaut.config-client.enabled'.toString()) == true
        commandContext.templates.get('k8sYaml')
    }

}
