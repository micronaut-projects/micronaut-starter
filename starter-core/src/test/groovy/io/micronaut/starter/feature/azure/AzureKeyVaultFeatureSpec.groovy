package io.micronaut.starter.feature.azure

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
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

    void "test azure-key-vault feature adds dependencies for buildTool=#buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["azure-key-vault"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.azure", "micronaut-azure-secret-manager")
        verifier.hasDependency("io.micronaut.discovery", "micronaut-discovery-client")

        where:
        buildTool << BuildTool.values()
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
