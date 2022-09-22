package io.micronaut.starter.feature.azure

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared

class AzureCosmosDbFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    AzureCosmosDbFeature feature = beanContext.getBean(AzureCosmosDbFeature)

    void 'test README.md with feature azure-cosmos-db contains links to micronaut and microsoft docs'() {
        when:
        def output = generate(['azure-cosmos-db'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Azure Cosmos DB documentation](https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureCosmosClient)")
        readme.contains("[https://learn.microsoft.com/en-us/azure/cosmos-db/](https://learn.microsoft.com/en-us/azure/cosmos-db/)")
    }

    void "feature azure-cosmos-db properties validation"() {
        expect:
        for (applicationType in ApplicationType.values())
            feature.supports(applicationType) == true
        feature.category == Category.DATABASE
        feature.name == 'azure-cosmos-db'
    }

    void "test maven azure-cosmos-db feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["azure-cosmos-db"])
                .render()
        def pom = new XmlSlurper().parseText(template)

        then:
        with(pom.dependencies.dependency.find { it.artifactId == "micronaut-azure-cosmos" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.azure'
        }
    }

    void "test gradle azure-cosmos-db feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["azure-cosmos-db"])
                .render()

        then:
        template.contains('implementation("io.micronaut.azure:micronaut-azure-cosmos")')
    }

    void "test azure-cosmos-db feature configuration"() {
        when:
        def config = buildGeneratorContext([feature.name]).configuration
        then:
        config.containsKey("azure.cosmos.endpoint")
        config.containsKey("azure.cosmos.key")
        config.containsKey("azure.cosmos.default-gateway-mode")
        config.containsKey("azure.cosmos.consistency-level")
        config.containsKey("azure.cosmos.endpoint-discovery-enabled")
    }
}
