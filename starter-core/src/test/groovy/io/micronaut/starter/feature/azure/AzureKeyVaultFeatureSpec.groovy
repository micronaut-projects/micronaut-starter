package io.micronaut.starter.feature.azure

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class AzureKeyVaultFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature azure-key-vault contains links to micronaut and microsoft docs'() {
        when:
        Map<String, String> output = generate(['azure-key-vault'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureKeyVault")
        readme.contains("https://azure.microsoft.com/en-us/services/key-vault/#product-overview")
    }

    void "test maven azure-key-vault feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["azure-key-vault"])
                .render()
        def pom = new XmlSlurper().parseText(template)

        then:
        with(pom.dependencies.dependency.find { it.artifactId == "micronaut-azure-secret-manager" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.azure'
        }
    }

    void "test gradle azure-key-vault feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["azure-key-vault"])
                .render()

        then:
        template.contains('implementation("io.micronaut.azure:micronaut-azure-secret-manager")')
    }

    void "test azure-key-vault feature configuration"() {
        when:
        AzureKeyVaultFeature feature = beanContext.getBean(AzureKeyVaultFeature)
        def config = buildGeneratorContext([feature.name]).bootstrapConfiguration

        then:
        config.containsKey("micronaut.application.name")
        config.containsKey("micronaut.config-client.enabled")
    }

}
