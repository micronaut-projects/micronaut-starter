package io.micronaut.starter.feature.distributedconfig

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class KubernetesConfigSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['config-kubernetes'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle kubernetes feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['config-kubernetes'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-discovery-client")')
        template.contains('implementation("io.micronaut:micronaut-management")')

        when:
        String pluginId = 'com.google.cloud.tools.jib'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven kubernetes feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['config-kubernetes'])
                .render()

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
        commandContext.bootstrapConfiguration.get('micronaut.application.name'.toString()) == 'foo'
        commandContext.bootstrapConfiguration.get('micronaut.config-client.enabled'.toString()) == true
        commandContext.templates.get('k8sYaml')
    }

    void 'test kubernetes no distributed config'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['kubernetes'])

        then:
        commandContext.bootstrapConfiguration.get('micronaut.config-client.enabled'.toString()) == null
        commandContext.templates.get('k8sYaml')
    }
}
