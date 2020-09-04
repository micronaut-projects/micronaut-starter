package io.micronaut.starter.feature.distributedconfig

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class KubernetesConfigSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes contains links to micronaut docs'() {
        when:
        def output = generate(['config-kubernetes'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle kubernetes feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['config-kubernetes'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-discovery-client")')
        template.contains('implementation("io.micronaut:micronaut-management")')
        template.contains('id "com.google.cloud.tools.jib"')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven kubernetes feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['config-kubernetes'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-discovery-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test kubernetes configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['config-kubernetes'])

        then:
        commandContext.bootstrapConfig.get('micronaut.application.name'.toString()) == 'foo'
        commandContext.bootstrapConfig.get('micronaut.config-client.enabled'.toString()) == true
        commandContext.templates.get('k8sYaml')
    }


    void 'test kubernetes no distributed config'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['kubernetes'])

        then:
        commandContext.bootstrapConfig.get('micronaut.config-client.enabled'.toString()) == null
        commandContext.templates.get('k8sYaml')
    }
}
