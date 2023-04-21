package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class DataAzureCosmosFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    DataAzureCosmosFeature feature = beanContext.getBean(DataAzureCosmosFeature)

    void 'test README.md with feature data-azure-cosmos contains links to micronaut and microsoft docs'() {
        when:
        def output = generate(['data-azure-cosmos'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Data Azure Cosmos documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/#azureCosmos)")
        readme.contains("[https://learn.microsoft.com/en-us/azure/cosmos-db/](https://learn.microsoft.com/en-us/azure/cosmos-db/)")
    }

    void "feature data-azure-cosmos properties validation"() {
        expect:
        for (applicationType in ApplicationType.values())
            feature.supports(applicationType)
        feature.category == Category.DATABASE
        feature.name == 'data-azure-cosmos'
    }

    void "test #buildTool data-azure-cosmos feature adds dependency and annotation processor"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["data-azure-cosmos"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.data", "micronaut-data-azure-cosmos", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-document-processor", Scope.ANNOTATION_PROCESSOR)

        where:
        buildTool << BuildTool.values()
    }

    void "test data-azure-cosmos feature configuration"() {
        when:
        def config = buildGeneratorContext([feature.name]).configuration
        then:
        !config.containsKey("azure.cosmos.endpoint")
        !config.containsKey("azure.cosmos.key")
        config.containsKey("azure.cosmos.default-gateway-mode")
        config.containsKey("azure.cosmos.consistency-level")
        config.containsKey("azure.cosmos.endpoint-discovery-enabled")
        config.containsKey("azure.cosmos.database.database-name")
        config.containsKey("azure.cosmos.database.update-policy")
        config["azure.cosmos.database.database-name"] == "myDb"
        config["azure.cosmos.database.update-policy"] == "NONE"
    }
}